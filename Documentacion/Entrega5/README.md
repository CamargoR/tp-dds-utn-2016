### Justificaciones del tipo de base de datos utilizada:

1) decidimos usar una base de datos del tipo documental (mongodb), ya que no existen joins y nos permite escalar horizontalmente(sharding) para así poder trabajar con grandes cantidades de datos
Para esto creamos una colección “Busqueda”, con sus documentos (historial de consultas), y cada búsqueda tiene sus atributos como el usuario que realizó la búsqueda, texto buscado, puntos de interés encontrados, fecha que se consultó y el tiempo de respuesta.


2) y 3) Pensamos que al usar una base de datos tipo clave-valor,y aprovechando la cache ultra-rápida que provee redis, persistiendo de esta manera a los resultados de los servicios externos, lo cual aumentaría el tiempo de respuesta al usuario final.
