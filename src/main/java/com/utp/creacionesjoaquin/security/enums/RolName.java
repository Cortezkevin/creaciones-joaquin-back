package com.utp.creacionesjoaquin.security.enums;

public enum RolName {
    ROLE_ADMIN , ROLE_USER, ROLE_TRANSPORT, ROLE_WAREHOUSE;

    public String getCapitalizedName() {
        String[] words = this.name().split("_");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            capitalized.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return capitalized.toString().trim();
    }
}
