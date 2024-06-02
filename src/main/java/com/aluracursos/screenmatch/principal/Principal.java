package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ff39da72";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries =new ArrayList<>();
    private SerieRepository repositorio;

    private List<Serie> series;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar serie por titulo
                    5 - Top5 mejores pelicuas
                    6 - Buscar series por categoria
                    7 - Filtrar series por Num de temporadas y evaluacion
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4 :
                    buscarSeriePortitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7 :
                    filtrarSeriePorTemporadaEvaluacion();
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }



    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        String urlCompleta = URL_BASE+nombreSerie.replace(" ","+")+API_KEY;
        System.out.println("urlComple "+urlCompleta);
        var json = consumoApi.obtenerDatos(urlCompleta);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("De que seria quieres ver sus episodios");
        var nombre = teclado.nextLine();
        Optional<Serie> serie = series.stream()
                .filter(s->s.getTitulo().toLowerCase().contains(nombre.toLowerCase()))
                .findFirst();
        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            //Convierto la lista de DatosTemporadas en  unan lista unica
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d->d.episodios().stream()
                            .map(e->new Episodio(d.numero(),e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }

    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        System.out.println("como queda seria "+serie);
        System.out.println(serie.episodios);
        //datosSeries.add(datos);
        System.out.println(datos);
    }
    private void mostrarSeriesBuscadas() {
       // datosSeries.forEach(System.out::println);
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
    private void buscarSeriePortitulo() {
        System.out.println("Escribe el nombre d ela seria que deseas buscar");
        var nombreSerie = teclado.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        //Optional<Serie> serieBuscada = repositorio.buscar(nombreSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("La serie buscada es "+ serieBuscada.get());
        }
        else{
            System.out.println("Serie no encontrada");
        }
    }
    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s-> System.out.println("Serie "+s.getTitulo()+
                " Evaluacion "+s.getEvaluacion()));

    }
    private void buscarSeriesPorCategoria() {
        System.out.println("escribe el genero/categoria de la seria que desa buscar");
        var categoriaBuscaria  = teclado.nextLine();
        var categoria = Categoria.fromEspaniol(categoriaBuscaria);
        List<Serie> seriesPorCategorias = repositorio.findByGenero(categoria);
        System.out.println("Las series de categorai "+categoriaBuscaria);
        seriesPorCategorias.forEach(s-> System.out.println(s));
    }
    private void filtrarSeriePorTemporadaEvaluacion() {
        System.out.println("Ingrese el numero de temproada");
        var numTemporadaQue = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Ingrese el numero de evaluacion");
        var numEvaluacionQue = teclado.nextDouble();
        List<Serie> seriesFiltradas = repositorio.seriesPorTemporadaYEvaluacion(numTemporadaQue,numEvaluacionQue);
        System.out.println("***Series Filtradas");
        seriesFiltradas.forEach(e-> System.out.println(e));
    }
}

