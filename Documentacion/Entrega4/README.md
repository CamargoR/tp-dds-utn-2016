### Persistencia de Puntos de Interes:

En nuestro modelo de objetos los puntos de interés están representados por una clase abstracta PuntoDeInteres que contiene el nombre del punto de interés, su ubicación, la fecha en que fue dado de baja (Si estuviese dado de baja) y las palabras clave con las que se lo puede buscar.

De esta clase abstracta heredan las clases LocalComercial, ParadaDeColectivo y otra clase abstracta PuntoDeInteresConServicios de la que heredan las clases CGP y Banco.

La clase CGP tiene una zona de cobertura. Mientras que el Banco tiene un horario de atencion. La clase LocalComercial posee un horario de atención y un rubro.

---

Para persistir este modelo de objetos en una base de datos relacional se propusieron dos  modelos posibles, uno aplicando una single-table y otro aplicando joined-tables.

Con respecto a estos modelos entendimos que el uso de una single-table facilitaba las consultas que debíamos hacer a la base de datos a expensas de tener tablas más pesadas, requerir un campo que distinga a qué tipo de punto de interés nos estamos refiriendo y que algunos campos queden asignados nulos cuando no se correspondan al tipo de punto de interés al que nos referimos. A su vez, la forma de distinguir el tipo de punto de interés podría indicarse mediante un id que referencie a una tabla que contenga todos los tipos de punto de interés o indicarse la clase a la que pertenece mediante un atributo VARCHAR.

Por otro lado, usar joined-tables nos permitiría tener tablas más ordenadas y ligeras pero requerirían un esfuerzo mayor a la hora de realizar las consultas a la base de datos, esto se debe a que al tener distintas tablas para cada tipo de punto de interés las consultas que se deban realizar polimórficamente se tornan complejas al tener que hacer joins entre tablas.

Debido a que estas clases conforman una misma entidad dentro del dominio del problema, ya que tienen comportamientos similares, fueron modeladas mediante herencia de clases. Sin embargo comparten pocos atributos entre sí por lo que a la hora de adaptar el modelo de objetos finalmente se optó por la solución mediante joined-tables, usando las annotations del JPA @Inheritance(strategy = InheritanceType.JOINED) en las superclases PuntoDeInteres y PuntoDeInteresConServicios.

En el dominio del problema entendemos que el uso más habitual será el de búsqueda de los puntos de interés a través de sus palabras clave y según la cercanía que tengan con respecto a la ubicación del usuario que realice la búsqueda. Por lo que la dificultad adicional y la pérdida de performance que supone hacer consultas polimórficas entre los puntos de interés no debería suponer una desventaja mayor que los beneficios que trae.

---

###Persistencia de las búsquedas de los puntos de interés.

Nuestros objetos de Búsqueda están compuestos por el usuario que la realiza, el texto que ingresó para realizar la búsqueda, la fecha en que se hizo, el tiempo de respuesta y el conjunto de puntos de interés que fueron encontrados.

En este caso nos encontramos con que las bases de datos relacionales no poseen mecanismos con los que representar la composición de objetos, por lo que para representar la relación de que una búsqueda contiene múltiple puntos de interés, y que un mismo punto de interés puede estar en muchas búsquedas es necesario crear una tabla intermedia PuntoDeInteresEncontrado que indique que puntos de interés pertenecen a cada búsqueda.

Para indicar al JPA que es necesario crear esta tabla intermedia se usaron las siguientes annotations dentro de la clase Búsqueda:

    `@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)`
    `@JoinTable(name = "PuntoDeInteresEncontrado")`
    `private Set<PuntoDeInteres> puntosDeInteresEncontrados;`

Como dentro de nuestro modelo no pueden existir búsquedas sin un conjunto de puntos de interés encontrados (Aunque sea debe tener un conjunto vacío). Se decidió usar el FetchType.EAGER que se asegura de traer el conjunto de datos al momento de acceder a la base de datos, en contraposición con FetchType.LAZY que se encarga de obtener el conjunto de datos al momento de acceder a ellos.


---


###Persistencia de los horarios de atención

La lógica de los horarios de atención fue modelada como un objeto de la clase HorarioDeAtencion que contiene un conjunto de objetos DiaYHorarioDeAtencion, el cual está compuesto por el día de la semana y el conjunto de RangoDeHorario en el que se atiende ese día.

En este caso al pasar la relación de composición entre objetos HorarioDeAtencion-DiaYHorarioDeAtencion y DiaYHorarioDeAtencion-RangoDeHorario al modelo relacional de la base de datos, se convirtieron estas relaciones del tipo “uno a muchos”, por lo que se usaron las siguientes annotations del JPA:

`Clase: HorarioDeAtencion`
    `@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)`
    `@JoinColumn(name = "puntoInteres_id")`
    `private Set<DiaYHorarioDeAtencion> horariosDeAtencionPorDia;`

`Clase: DiaYHorarioDeAtencion`
    `@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)`
    `@JoinColumn(name = "diaYhorarioDeAtencion_id")`
    `private Set<RangoDeHorario> rangosDeHorario;`

Con la annotation @JoinColumn(name = “xxx”) le indicamos al JPA el nombre de la columna que queremos emplear como foreign key.

En ambos casos nos encontramos a una situación similar a la que ocurría con las búsquedas. Los objetos HorarioDeAtencion no tienen sentido de ser dentro del dominio del problema si no tienen horariosDeAtencionPorDia, ni los objetos DiaYHorarioDeAtencion sin rangosDeHorario; por esto se decidió usar FetchType.EAGER.
