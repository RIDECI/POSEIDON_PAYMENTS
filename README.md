# POSEIDON_PAYMENTS_BACKEND

## Desarrolladores 

* Deisy Lorena Guzman Cabrales
* Diego Fernando Chavarro Castillo
* Oscar Andres Sanchez Porras
* Samuel Leonardo Albarrachin Vergara
* Sergio Alejandro Idarraga

---

**Descripción:**

Es el componente encargado de gestionar todas las operaciones financieras dentro de RidECI. Administra el registro, procesamiento y consulta de pagos realizados por los pasajeros hacia los conductores o hacia la plataforma, ya sea a través de medios digitales (Nequi, tarjeta) o en efectivo.
Además, controla transacciones, estados de pago, reembolsos, comprobantes y conciliación, garantizando seguridad, trazabilidad e integridad de la información económica del sistema

---

## Funcionamiento del Módulo de Pagos:

###  Procesamiento de pagos
- Registra los pagos asociados a reservas o viajes confirmados.
- Procesa pagos mediante Nequi, tarjeta, Llaves Bre-B o efectivo.
- Valida los datos del pago antes de autorizarlo (monto, método, usuarios).
- Actualiza el estado del pago: PENDIENTE → AUTORIZADO → PROCESADO → APROBADO → COMPLETADO.
- Genera comprobantes digitales para cada transacción realizada.

### Gestión de transacciones
- Consulta el historial de transacciones filtrado por usuario, viaje o fecha.
- Registra todos los eventos del ciclo de vida del pago para auditoría.
- Detecta pagos duplicados, fallidos o inconsistentes.
- Integra con el módulo de reservas para confirmar o liberar cupos automáticamente.

### Reembolsos
- Permite solicitar reembolsos desde una reserva o viaje cancelado.
- Gestiona todo el flujo del reembolso:
  - AUTORIZAR
  - PROCESAR
  - APROBAR
  - COMPLETAR
- Valida que el pago cumpla las políticas de devolución.
- Registra el reembolso como transacción independiente del pago original.

### Notificaciones
- Envía notificaciones cuando un pago sea aprobado, fallido o reembolsado.
- Se integra con el módulo de notificaciones para alertas en tiempo real.

### Restricciones de negocio
- No se procesa un pago sin una reserva o viaje asociado.
- Los reembolsos solo aplican si el pago original está en estado COMPLETADO.
- No se avanza al siguiente estado del pago sin completar el anterior.
- Los pagos en efectivo deben ser confirmados manualmente.
- No se permite modificar una transacción completada; únicamente se puede reembolsar.

---
# Modulo Necesarios:

**Autenticación:**

Se utiliza para gestionar la información de los usuarios y roles. Permite listar usuarios, aprobarlos, rechazarlos, bloquearlos, obtener detalles de un usuario, entre otras acciones.


**Manejo de viajes:**

Se requiere para conocer el estado de los viajes (inicio, finalización) y también para calcular el monto que los usuarios deben pagar por cada servicio

**Notificaciones:**

Encargado de enviar notificaciones a los usuarios sobre confirmaciones, rechazos o cualquier inconveniente relacionado con pagos o viajes.


---

## Tabla de Contenidos

* [ Estrategia de Versionamiento y Branching](#-estrategia-de-versionamiento-y-branching)

    * [ Estrategia de Ramas (Git Flow)](#-estrategia-de-ramas-git-flow)
    * [ Convenciones de Nomenclatura](#-convenciones-de-nomenclatura)
    * [ Convenciones de Commits](#-convenciones-de-commits)
* [ Arquitectura del Proyecto](#-arquitectura-del-proyecto)

    * [ Estructura de Capas](#️-estructura-de-capas)
* [ Tecnologías Utilizadas](#️-tecnologías-utilizadas)
* [ Arquitectura Limpia - Organización de Capas](#️-arquitectura-limpia---organización-de-capas)
* [Diagramas del Módulo](#diagramas-del-módulo)
* - [Ejecución Local](#ejecución-local)  
* - [Calidad y CI/CD](#calidad-y-cicd)

---

##  Estrategia de Versionamiento y Branching

Se implementa una estrategia de versionamiento basada en **GitFlow**, garantizando un flujo de desarrollo **colaborativo, trazable y controlado**.

###  Beneficios:

- Permite trabajo paralelo sin conflictos
- Mantiene versiones estables y controladas
- Facilita correcciones urgentes (*hotfixes*)
- Proporciona un historial limpio y entendible

---

##  Estrategia de Ramas (Git Flow)

| **Rama**                | **Propósito**                            | **Recibe de**           | **Envía a**        | **Notas**                      |
| ----------------------- | ---------------------------------------- | ----------------------- | ------------------ | ------------------------------ |
| `main`                  | Código estable para PREPROD o Producción | `release/*`, `hotfix/*` | Despliegue         | Protegida con PR y CI exitoso  |
| `develop`               | Rama principal de desarrollo             | `feature/*`             | `release/*`        | Base para integración continua |
| `feature/*`             | Nuevas funcionalidades o refactors       | `develop`               | `develop`          | Se eliminan tras el merge      |
| `release/*`             | Preparación de versiones estables        | `develop`               | `main` y `develop` | Incluye pruebas finales        |
| `bugfix/*` o `hotfix/*` | Corrección de errores críticos           | `main`                  | `main` y `develop` | Parches urgentes               |

---

##  Convenciones de Nomenclatura

### Feature Branches

```
feature/[nombre-funcionalidad]-atenea_[codigo-jira]
```

**Ejemplos:**

```
- feature/authentication-module-atenea_23
- feature/security-service-atenea_41
```

**Reglas:**

*  Formato: *kebab-case*
*  Incluir código Jira
*  Descripción breve y clara
*  Longitud máxima: 50 caracteres

---

### Release Branches

```
release/[version]
```

**Ejemplos:**

```
- release/1.0.0
- release/1.1.0-beta
```

---

### Hotfix Branches

```
hotfix/[descripcion-breve-del-fix]
```

**Ejemplos:**

```
- hotfix/fix-token-expiration
- hotfix/security-patch
```

---

## Convenciones de Commits

### Formato Estándar

```
[codigo-jira] [tipo]: [descripción breve de la acción]
```

**Ejemplos:**

```
45-feat: agregar validación de token JWT
46-fix: corregir error en autenticación por roles
```

---

### Tipos de Commit

| **Tipo**   | **Descripción**                      | **Ejemplo**                                     |
| ----------- | ------------------------------------ | ----------------------------------------------- |
| `feat`      | Nueva funcionalidad                  | `22-feat: implementar autenticación con JWT`    |
| `fix`       | Corrección de errores                | `24-fix: solucionar error en endpoint de login` |
| `docs`      | Cambios en documentación             | `25-docs: actualizar README con nuevas rutas`   |
| `refactor`  | Refactorización sin cambio funcional | `27-refactor: optimizar servicio de seguridad`  |
| `test`      | Pruebas unitarias o de integración   | `29-test: agregar tests para AuthService`       |
| `chore`     | Mantenimiento o configuración        | `30-chore: actualizar dependencias de Maven`    |


**Reglas:**

* Un commit = una acción completa
* Máximo **72 caracteres** por línea
* Usar modo imperativo (“agregar”, “corregir”, etc.)
* Descripción clara de qué y dónde
* Commits pequeños y frecuentes

---

## Arquitectura del Proyecto

El backend de **ATENEA_ADMINISTRATION_BACKEND** sigue una **arquitectura limpia y desacoplada**, priorizando:

* Separación de responsabilidades
* Mantenibilidad
* Escalabilidad
* Facilidad de pruebas

---

## Estructura de Capas

```
📂 poseidon_Payments
 📂 src/
  ┣ 📂 main/
  ┃ ┣ 📂 java/
  ┃ ┃ ┗ 📂 edu/dosw/rideci/
  ┃ ┃ ┃ ┣ 📂 application/
  ┃ ┃ ┃ ┃ ┣ 📂 events/
  ┃ ┃ ┃ ┃ ┃ ┣ 📂 command/
  ┃ ┃ ┃ ┃ ┃ ┗ 📂 listener/
  ┃ ┃ ┃ ┃ ┣ 📂 exceptions/
  ┃ ┃ ┃ ┃ ┣ 📂 port/
  ┃ ┃ ┃ ┃ ┃ ┣ 📂 in/
  ┃ ┃ ┃ ┃ ┃ ┗ 📂 out/
  ┃ ┃ ┃ ┃ ┗ 📂 service/
  ┃ ┃ ┃
  ┃ ┃ ┃ ┣ 📂 domain/
  ┃ ┃ ┃ ┃ ┗ 📂 model/
  ┃ ┃ ┃ ┃ ┃ ┣ 📂 enums/
  ┃ ┃ ┃ ┃ ┃ ┗ 📂 valueobjects/
  ┃ ┃ ┃
  ┃ ┃ ┃ ┣ 📂 infrastructure/
  ┃ ┃ ┃ ┃ ┣ 📂 adapters/
  ┃ ┃ ┃ ┃ ┃ ┣ 📂 messaging/
  ┃ ┃ ┃ ┃ ┃ ┗ 📂 persistence/
  ┃ ┃ ┃ ┃ ┣ 📂 configs/
  ┃ ┃ ┃ ┃ ┣ 📂 controller/
  ┃ ┃ ┃ ┃ ┃ ┗ 📂 dto/
  ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 Request/
  ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂 Response/
  ┃ ┃ ┃ ┃ ┣ 📂 exceptions/
  ┃ ┃ ┃ ┃ ┗ 📂 persistence/
  ┃ ┃ ┃ ┃ ┃ ┣ 📂 Entity/
  ┃ ┃ ┃ ┃ ┃ ┗ 📂 Repository/
  ┃ ┃ ┃ ┃ ┃ ┃  ┗ 📂 Mapper/
  ┃ ┃ ┃
  ┃ ┃ ┃ ┗ 📄 PoseidonPaymentsApplication.java

```

---

## Tecnologías Utilizadas

| **Categoría**              | **Tecnologías**                           |
| -------------------------- | ----------------------------------------- |
| **Backend**                | Java 17, Spring Boot, Maven               |
| **Base de Datos**          | MongoDB, PostgreSQL                       |
| **Infraestructura**        | Docker, Kubernetes (K8s), Railway, Vercel |
| **Seguridad**              | JWT, Spring Security                      |
| **Integración Continua**   | GitHub Actions, Jacoco, SonarQube         |
| **Documentación y Diseño** | Swagger UI, Figma                         |
| **Comunicación y Gestión** | Slack, Jira                               |
| **Testing**                | Postman                                   |

---

## Arquitectura Limpia - Organización de Capas

### DOMAIN (Dominio)

Representa el **núcleo del negocio**, define **qué hace el sistema, no cómo lo hace**.
Incluye entidades, objetos de valor, enumeraciones, interfaces de repositorio y servicios de negocio.

### APPLICATION (Aplicación)

Orquesta la lógica del negocio a través de **casos de uso**, **DTOs**, **mappers** y **excepciones personalizadas**.

### INFRASTRUCTURE (Infraestructura)

Implementa los **detalles técnicos**: controladores REST, persistencia, configuración, seguridad y conexión con servicios externos.

---

## Diagramas del Módulo


### Diagrama de Despliegue 

![DiagramaDespliegue](docs/uml/diagramaDespliegue.png)


### Backend y Despliegue

- Desarrollado en Java con Spring Boot.

- Desplegado automáticamente en Railway mediante un pipeline de CI/CD con GitHub Actions.

##  Almacenamiento en PostgreSQL

El microservicio de Pagos utiliza **PostgreSQL** como base de datos principal para almacenar información crítica relacionada con las operaciones financieras.  
Los datos almacenados incluyen:

 1. Transacciones de Pago
 2. Reembolsos
 3. Auditoría y Eventos
 4. Métodos de Pago Asociados
 5. Relación con Viajes y Reservas

PostgreSQL asegura **consistencia, integridad referencial y trazabilidad**, esenciales para el manejo seguro de operaciones financieras dentro de la plataforma RidECI.


### Calidad del Código

- Integra JaCoCo para medir cobertura de pruebas.

- Utiliza SonarQube para análisis estático y detección de vulnerabilidades.

## Funcionalidades Principales del Módulo de Pagos

- Gestión de Pagos

- Gestión de Reembolsos

- Consultas y Auditoría

- Seguridad y Validaciones

---

### Diagrama de Componentes General

![alt text](docs/uml/DiagramaComponentesGeneral.png)


#### **Frontend:** 
 
Desarrollado en TypeScript y desplegado en Vercel".


#### **API Gateway:** 

Centraliza y gestiona las comunicaciones entre los componentes.


#### **Backend:** 

Gestiona la lógica de pagos, integrando JaCoco SonarQube para garantizar calidad de código y funcione de manera correcta para los conductores, viajes y usuarios.

Ademas usamos un Pipeline para validar que todo funcione como debe funcionar.

Desplieguemos en Railway para construir el Docker, usamos Swagger y PostMan para probar y spring boot para gestionar el proyecto de manera eficiente mediante una API REST flexible.

#### **Base de datos:** 

Utiliza PostgreSQL para almacenar datos institucionales.


---

### Diagrama de Componentes Específico 

![alt text](docs/uml/diagramaComponentesEspecificos.png)

El módulo de Pagos usa Arquitectura Hexagonal para mantener la lógica de negocio
independiente de frameworks y detalles técnicos. Esto facilita pruebas, actualizaciones y despliegues ágiles.

### Estructura y flujo

El frontend en React y TypeScript llama controladores que invocan casos de uso. Los casos de uso contienen la lógica central: aprobación de conductores, suspensión de usuarios y generación de reportes. Los casos de uso sólo dependen de puertos, manteniendo el core aislado.

### Puertos y adaptadores

Los puertos definen contratos para persistencia, publicación de eventos y notificaciones. Los adaptadores implementan esos contratos integrando con MongoDB, RabbitMQ y servicios externos de autenticación y reputación. Esto permite sustituir o simular implementaciones en pruebas.

### Auditoría y eventos

Todas las acciones administrativas se registran en auditoría y se propagan como eventos con identificadores de correlación y comandos para idempotencia y trazabilidad. El procesamiento asíncrono evita bloquear la operación principal.

### Políticas y extensibilidad

Las políticas de publicación se evalúan con un factory de estrategias. El patrón strategy permite añadir reglas como días permitidos, roles o excepciones sin tocar el core y facilita pruebas unitarias de cada regla.

### Ejemplo de flujo

Al aprobar un conductor el flujo va del frontend al caso de uso, que actualiza el repositorio, registra la acción en auditoría y publica un evento. Listeners consumen el evento para notificaciones, actualizaciones de reputación o generación de reportes sin impactar la operación inicial.


---


## Diagrama de Casos de Uso

![alt text](docs/uml/DiagramaCasosUso.png)

Las transacciones en el **Módulo de Pagos de RIDECI** permiten a los pasajeros realizar pagos seguros por sus viajes a través de Nequi, tarjeta, Llaves Bre-B o efectivo, y permiten a los conductores recibirlos de manera confiable.  

El sistema gestiona el ciclo completo de cada transacción, desde su creación hasta su autorización, procesamiento, aprobación y finalización, asegurando que cada operación quede correctamente registrada y asociada al viaje correspondiente.

Además, este módulo administra solicitudes de reembolso y ejecuta todo su flujo operativo (autorizar, procesar, aprobar y completar), siguiendo las políticas institucionales.  
También permite consultar el historial de pagos, generar comprobantes digitales, detectar inconsistencias, evitar duplicidad de transacciones y almacenar registros de auditoría para garantizar trazabilidad y seguridad financiera.

---

### Diagrama de Clases

![alt text](docs/uml/DiagramaClases.png)


## Patrones de diseño:

### Strategy

Representado por la interfaz PaymentStrategy y sus implementaciones (BreBPayment, NequiPayment, CashPayment, CardPayment). Permite definir diferentes algoritmos para procesar pagos según el método, intercambiables en tiempo de ejecución.

### Factory Method / Factory

Representado por PaymentMethodFactory que crea instancias concretas de PaymentStrategy según el tipo de pago (PaymentMethodType). Centraliza la lógica de creación de objetos para desacoplar al cliente de las implementaciones concretas.

### Command 

No se ve reflejado en el diagrama de clases pero se uso para los eventos ya que modela una accion la cual tenemos que
consumir para que sea ejecutado y sirva como por ejemplo con los eventos de inicio y fin de un viaje para 
que el administrador pueda actuar según la situación. 

---
## 🧱 Principios SOLID aplicados al microservicio de Pagos

### **Single Responsibility Principle (SRP)**

Cada componente del microservicio está diseñado para cumplir una única responsabilidad.  
Los controladores manejan únicamente la entrada HTTP, los casos de uso contienen solo la lógica de negocio,  
los adaptadores se concentran en la infraestructura, y los mapeadores se encargan exclusivamente de transformar datos.  
Esto garantiza clases pequeñas, claras y fáciles de mantener.

---

### **Open/Closed Principle (OCP)**

El sistema permite extender nuevas funcionalidades sin modificar lo existente.  
Es posible añadir nuevos métodos de pago, nuevas reglas de reembolso o nuevos pasos del flujo sin alterar el código ya implementado.  
La arquitectura facilita que el sistema crezca sin introducir regresiones.

---

### **Liskov Substitution Principle (LSP)**

Las clases que representan comportamientos similares pueden sustituirse entre sí sin romper el sistema.  
Las estrategias de pago funcionan de forma intercambiable y cualquier implementación puede utilizarse sin afectar la lógica del dominio.  
Esto ayuda a que el sistema sea flexible y adaptable a nuevos métodos.

---

### **Interface Segregation Principle (ISP)**

Las interfaces están divididas en contratos pequeños y específicos.  
Cada caso de uso define únicamente lo necesario para la operación que representa,  
evitando interfaces grandes, difíciles de implementar o con responsabilidades mezcladas.  
Los componentes solo dependen de lo que realmente necesitan.

---

### **Dependency Inversion Principle (DIP)**

El dominio depende exclusivamente de abstracciones y no de implementaciones concretas.  
Los casos de uso trabajan con interfaces que representan repositorios u operaciones externas,  
mientras que la infraestructura implementa estas interfaces sin afectar la lógica de negocio.  
Esto permite modificar tecnología, persistencia o framework sin tocar el dominio.

---

### Diagrama de Bases de Datos

![DiagramaBasesDatos](docs/uml/diagramaBaseDeDatos.png)

La estructura relacional normalizada en Tercera Forma Normal (3NF) permite: 

- Eliminar redundancia de datos mediante tablas especializadas 
- Garantizar integridad referencial a través de foreign keys 
- Facilitar auditoría con registros inmutables de cada transacción 
- Optimizar consultas mediante índices estratégicamente ubicados 
Las cinco tablas principales son: 
- TRANSACTION: Tabla central que registra todas las transacciones de pago (Nequi y efectivo), con códigos únicos de comprobante y referencias a servicios externos. 
- PAYMENT_METHOD: Almacena los métodos de pago Nequi guardados por los usuarios para reutilización futura, encriptando datos sensibles. 
- REFUND: Registra los reembolsos procesados, manteniendo trazabilidad completa mediante relación con la transacción original. 
- CASH_PAYMENT_CONFIRMATION: Exclusiva para pagos en efectivo, permite al conductor confirmar la recepción del dinero con timestamp y observaciones. 
- PAYMENT_RECEIPT: Almacena los comprobantes de pago generados automáticamente, incluyendo un snapshot completo de la información en formato JSON para preservar el estado exacto al momento de emisión. 

---
### Sequence Diagrams

Los diagramas de secuencias estan enfocados en seguir la estructura limpia del proyecto siguiendo el el siguiente flujo:

- Controller
- Use Case
- Repository Port
- Repository Adapter
- Mongo Repository 

Luego usa la base de datos Mongo para evidenciar los documentos 

📄 [Ver diagrama de secuencia](docs/pdf/diagramaSecuencias.pdf)

---


## Diagrama de Contexto

![alt text](docs/uml/DiagramaContexto.png)

El Módulo de  Pagos permite a los pasajeros gestionar sus viajes desde el pago.

Pasajeros:
Pueden realizar pagos de viajes, además de buscar, reservar, cancelar y calificar a los conductores.

Administrador:
Tiene la capacidad de monitorear y hacer seguimiento a todas las transacciones de pago realizadas en la plataforma.

Conductores:
Reciben pagos y pueden recibir calificaciones y recomendaciones de los pasajeros, que podrían influir en futuros pagos o viajes.

---
# Getting Started

### Requesitos
- Java 17
- Maven 3.X
- Docker + Docker Compose
- Puerto disponiblo 8080

### Clone & open repository

`git clone https://github.com/RIDECI/POSEIDON_PAYMENTS`

`cd POSEIDON_PAYMENTS`

### Dockerize the project

Dockerize before compile the project avoid configuration issues and ensure environment consistency.

``` bash
docker compose up -d
```

[Ver video demostrativo]()

### Install dependencies & compile project

Download dependencies and compile the source code.

``` bash
mvn clean install
```

``` bash
mvn clean compile
```

### To run the project
Start the Spring Boot server

``` bash
docker-compose up --build -d   
```

--- 

#### Prueba de Ejecución Local:

[Ver video demostrativo]()



---
# 🧪 Testing

Testing is a essential part of the project functionability, this part will show the code coverage and code quality analazing with tools like JaCoCo and SonarQube.

### 📊 Code Coverage (JaCoCo)

---

[Ver video de cobertura y jacoco]()

![JaCoCo](docs/imagenes/jacoco.png)


![JaCoCo](docs/imagenes/jacocoCaseUse.png)


![JaCoCo](docs/imagenes/jacocoController.png)





### 🔍 Static Analysis (SonarQube)

---

[Ver video de cobertura de sonar]()

![SonarQube](docs/imagenes/sonarQube.png)

![SonarQube](docs/imagenes/sonarQubec.png)



### 💻  Evidence Postman

---

[Ver video de cobertura de sonar]()

**RIDECI** - Conectando a la comunidad para moverse de forma segura, económica y sostenible.