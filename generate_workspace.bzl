# The following dependencies were calculated from:
#
# generate_workspace -a org.apache.kafka:kafka-clients:1.0.0 -r http://repo.maven.apache.org/maven2/


def generated_maven_jars():
  # org.apache.kafka:kafka-clients:jar:1.0.0
  native.maven_jar(
      name = "org_lz4_lz4_java",
      artifact = "org.lz4:lz4-java:1.4",
      repository = "http://repo.maven.apache.org/maven2/",
      sha1 = "9bedb74f461a87ff2161bdf0778ad8ca6bad3e1c",
  )


  # org.apache.kafka:kafka-clients:jar:1.0.0
  native.maven_jar(
      name = "org_slf4j_slf4j_api",
      artifact = "org.slf4j:slf4j-api:1.7.25",
      repository = "http://repo.maven.apache.org/maven2/",
      sha1 = "da76ca59f6a57ee3102f8f9bd9cee742973efa8a",
  )


  # org.apache.kafka:kafka-clients:jar:1.0.0
  native.maven_jar(
      name = "org_xerial_snappy_snappy_java",
      artifact = "org.xerial.snappy:snappy-java:1.1.4",
      repository = "http://repo.maven.apache.org/maven2/",
      sha1 = "d94ae6d7d27242eaa4b6c323f881edbb98e48da6",
  )


  native.maven_jar(
      name = "org_apache_kafka_kafka_clients",
      artifact = "org.apache.kafka:kafka-clients:1.0.0",
      repository = "http://repo.maven.apache.org/maven2/",
      sha1 = "98a12cfff3a5b7ca77e68aba7adccfd817b2d846",
  )




def generated_java_libraries():
  native.java_library(
      name = "org_lz4_lz4_java",
      visibility = ["//visibility:public"],
      exports = ["@org_lz4_lz4_java//jar"],
  )


  native.java_library(
      name = "org_slf4j_slf4j_api",
      visibility = ["//visibility:public"],
      exports = ["@org_slf4j_slf4j_api//jar"],
  )


  native.java_library(
      name = "org_xerial_snappy_snappy_java",
      visibility = ["//visibility:public"],
      exports = ["@org_xerial_snappy_snappy_java//jar"],
  )


  native.java_library(
      name = "org_apache_kafka_kafka_clients",
      visibility = ["//visibility:public"],
      exports = ["@org_apache_kafka_kafka_clients//jar"],
      runtime_deps = [
          ":org_lz4_lz4_java",
          ":org_slf4j_slf4j_api",
          ":org_xerial_snappy_snappy_java",
      ],
  )


