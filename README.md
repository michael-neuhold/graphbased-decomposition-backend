# Graph-based decomposition of monolithic architectures - a path to microservice architectures

## Idea

This [project](https://github.com/gmazlami/microserviceExtraction-backend) was originally implemented by Genc Mazlami in the 
scope of a master thesis at the Department of Informatics at the University of Zurich.

As mentioned in the [README.md](https://github.com/gmazlami/microserviceExtraction-backend/blob/master/README.md) of the original 
project, further modifications can be done for academic purposes - as it applies in this case.
````
Any further use, modificaion or copy is allowed solely for academic purposes. All rights belong to Genc Mazlami.
````

## API
The application exposes three resources:
- [Repository](./src/main/java/monolith2microservice/inbound/RepositoryController.java)
- [Decomposition](./src/main/java/monolith2microservice/inbound/DecompositionController.java)
- [Evaluation](./src/main/java/monolith2microservice/inbound/EvaluationController.java)

## How to start
As data persistence, a postgres database is used. To get started, you have to execute the following command:
````SHELL
docker run \
    --name graph-based-decomposition-db \
    -e POSTGRES_PASSWORD=<insert password> \
    -e POSTGRES_USER=<insert user> \
    -p 5432:5432 \
    -d postgres:latest
````
After starting the project as spring boot application the Swagger documentation can be found [here](http://localhost:8080/swagger-ui.html).

## Visualization
The functionality provided via the REST API was used to implement a web application. The [frontend project](https://github.com/michael-neuhold/graphbased-decomposition-frontend)
wasa developed to provide a better usability for decomposing and analyzing results. 

