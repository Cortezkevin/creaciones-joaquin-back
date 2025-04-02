package com.utp.creacionesjoaquin.config.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.utp.creacionesjoaquin.config.cloudinary.dto.UploadDTO;
import com.utp.creacionesjoaquin.config.cloudinary.utils.UploadUtils;
import com.utp.creacionesjoaquin.administration.dto.product.UploadResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${cloudinary.upload.preset}")
    private String uploadPreset;

    public boolean delete(String public_id){
        try {
            Map res = cloudinary.uploader().destroy(public_id, ObjectUtils.emptyMap());
            System.out.println(res.toString());
            return true;
        }catch (IOException ex){
            log.error( ex.getMessage() );
            return false;
        }
    }

    public List<UploadResultDTO> uploadMany2(String folderName, List<UploadDTO> uploadDTOList) {
        try {
            List<UploadResultDTO> results = new ArrayList<>();
            for (UploadDTO dto : uploadDTOList) {
                System.out.println("FILE NAME: " + dto.file().getName() );
                String publicId = folderName + "/" + UploadUtils.formatFileName(dto.name());
                Map params = ObjectUtils.asMap(
                        "upload_preset", uploadPreset,
                        "public_id", publicId
                );

                Map res = cloudinary.uploader().upload(dto.file(), params);

                results.add(new UploadResultDTO( publicId , res.get("secure_url").toString())) ;
            }

            return results;
        }catch (IOException ex){
            log.error( ex.getMessage() );
            return null;
        }
    }

    public UploadResultDTO upload(String folderName, UploadDTO uploadDTO) {
        try {
            String publicId = folderName + "/" + UploadUtils.formatFileName(uploadDTO.name());
            Map params = ObjectUtils.asMap(
                    "upload_preset", uploadPreset,
                    "public_id", publicId
            );
            Map res = cloudinary.uploader().upload( uploadDTO.file(), params);
            return new UploadResultDTO(publicId, res.get("secure_url").toString());
        }catch (IOException ex){
            log.error( ex.getMessage() );
            return null;
        }
    }
}
