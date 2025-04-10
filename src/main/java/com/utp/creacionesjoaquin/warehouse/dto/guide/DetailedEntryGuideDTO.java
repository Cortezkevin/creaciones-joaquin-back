package com.utp.creacionesjoaquin.warehouse.dto.guide;

import com.utp.creacionesjoaquin.purchase.dto.purchaseOrder.PurchaseOrderDTO;
import com.utp.creacionesjoaquin.warehouse.dto.warehouse.MinimalInventoryMovementDTO;
import com.utp.creacionesjoaquin.warehouse.model.EntryGuide;

import java.sql.Timestamp;
import java.util.List;

public record DetailedEntryGuideDTO (
        String id,
        Timestamp date,
        String productConditions,
        String grocer,
        PurchaseOrderDTO purchaseOrder,
        List<MinimalInventoryMovementDTO> movementsList,
        String warehouse
){
    public static DetailedEntryGuideDTO parseToDTO(EntryGuide entryGuide){
        return new DetailedEntryGuideDTO(
                entryGuide.getId(),
                entryGuide.getDate(),
                entryGuide.getProductConditions(),
                entryGuide.getGrocer().getUser().getPersonalInformation().getFullName(),
                entryGuide.getPurchaseOrder() != null ? PurchaseOrderDTO.parseToDTO( entryGuide.getPurchaseOrder() ) : null,
                entryGuide.getInventoryMovementsList().stream().map( MinimalInventoryMovementDTO::parseToDTO ).toList(),
                entryGuide.getWarehouse().getLocation()
        );
    }
}
