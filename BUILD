java_binary(
    name = "Kafka_Producer",
    srcs = ["Kafka_Producer.java"],
    main_class = "Kafka_Producer",
    deps = [
        "@org_apache_kafka_kafka_clients//jar",
        "@org_lz4_lz4_java//jar",
        "@org_slf4j_slf4j_api//jar",
        "@org_xerial_snappy_snappy_java//jar",
    ],
)

java_binary(
    name = "Kafka_Consumer",
    srcs = glob(["**/*.java"]),
    main_class = "Kafka_Consumer",
    deps = [
        "Kafka_Producer",
        "@org_apache_kafka_kafka_clients//jar",
        "@org_lz4_lz4_java//jar",
        "@org_slf4j_slf4j_api//jar",
        "@org_xerial_snappy_snappy_java//jar",
    ],
)
