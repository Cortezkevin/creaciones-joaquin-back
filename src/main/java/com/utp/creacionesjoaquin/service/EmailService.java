package com.utp.creacionesjoaquin.service;

import com.utp.creacionesjoaquin.dto.ResponseWrapperDTO;
import com.utp.creacionesjoaquin.exception.customException.ResourceNotFoundException;
import com.utp.creacionesjoaquin.security.model.User;
import com.utp.creacionesjoaquin.security.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${front.url}")
    private String FRONT_PATH;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;

    public ResponseWrapperDTO<String> sendHtmlTemplateEmail(String to ){
        try {
            User user = userRepository.findByEmail( to ).orElseThrow(() -> new ResourceNotFoundException("Ingrese un correo que este registrado"));
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                Context context = new Context();

                UUID token = UUID.randomUUID();
                user.setTokenPassword( token.toString() );

                User userUpdate = userRepository.save( user );

                Map<String, Object> model = new HashMap<>();
                model.put("username", userUpdate.getPersonalInformation().getFirstName());
                /* https://creaciones-joaquin-front.vercel.app/ */
                /* http://localhost:3000/ */
                model.put("url", FRONT_PATH + "/auth/change-password/confirm/" + token);
                context.setVariables( model );

                String htmlText =  templateEngine.process("email_template", context);
                helper.setFrom("cortezkevinq@gmail.com");
                helper.setTo(to);
                helper.setSubject("Prueba envio email");
                helper.setText(htmlText, true);

                javaMailSender.send(mimeMessage);
                return ResponseWrapperDTO.<String>builder()
                        .message("Correo de confirmacion enviado, revisa tu buzon")
                        .success(true)
                        .status(HttpStatus.OK.name())
                        .content("Email de confirmacion enviado")
                        .build();
            }catch (MessagingException e) {
                return ResponseWrapperDTO.<String>builder()
                        .message("Ocurrio un error al enviar el correo")
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .content(null)
                        .build();
            }
        }catch (ResourceNotFoundException e){
            return ResponseWrapperDTO.<String>builder()
                    .message(e.getMessage())
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.name())
                    .content(null)
                    .build();
        }
    }
}
