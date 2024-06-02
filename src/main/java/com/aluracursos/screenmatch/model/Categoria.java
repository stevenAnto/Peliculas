package com.aluracursos.screenmatch.model;

public enum Categoria {
    ACCION("Action"),
    ROMANCO("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIMEN("Crimen");
    private String categoriaOmd;
    Categoria(String categoriaOmd){
        this.categoriaOmd = categoriaOmd;
    }

    public static Categoria fromString(String text){
        for(Categoria c: Categoria.values()){
            if(c.categoriaOmd.equalsIgnoreCase(text)){
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria no encontrada "+text);
    }
}
