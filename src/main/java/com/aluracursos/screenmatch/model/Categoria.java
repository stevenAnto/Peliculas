package com.aluracursos.screenmatch.model;

public enum Categoria {
    ACCION("Action","Acción"),
    ROMANCO("Romance","Romance"),
    COMEDIA("Comedy","Comedia"),
    DRAMA("Drama","Drama"),
    CRIMEN("Crimen","Crimen");
    private String categoriaOmd;
    private String categoriaEspaniol;
    Categoria(String categoriaOmd, String categoriaEspaniol){
        this.categoriaOmd = categoriaOmd;
        this.categoriaEspaniol =categoriaEspaniol;
    }

    public static Categoria fromString(String text){
        for(Categoria c: Categoria.values()){
            if(c.categoriaOmd.equalsIgnoreCase(text)){
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria no encontrada "+text);
    }
    public static Categoria fromEspaniol(String text){
        for(Categoria c: Categoria.values()){
            if(c.categoriaEspaniol.equalsIgnoreCase(text)){
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria no encontrada "+text);
    }

}
