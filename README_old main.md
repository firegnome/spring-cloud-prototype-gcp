# Studienarbeit

Architecture-Prototype of a Spring Cloud Application

>>>
stop clusters
```
gcloud container clusters resize devtools --size=0
gcloud container clusters resize application --size=0
```

start clusters
```
gcloud container clusters resize devtools --size=1
gcloud container clusters resize application --size=1
```
>>>

## Technology-Prototypes

Evaluating spring cloud and google cloud technologies, these [examples](examples) were made.

## Development Tools

The Project uses following development tools:

* Jenkins
* SonarQube

They are documented under [devtools](devtools)

## Links

* [Documentation](https://confluence.skynut.ch/)

## Contributors

* **Moritz Habegger**
* **Micha Schena**

## License

This project is made avaible under the MIT License. See the [LICENSE](LICENSE) file for the full license.

The logos used in the diagrams are registered trademarks of Google LLC (Google Cloud), 
Pivotal Software Inc (Spring), Docker Inc. (Docker), Salvatore Sanfilippo (Redis)
and Igor Sysoev (nginx).