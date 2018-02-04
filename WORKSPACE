#maven_server(
#  name = "default",
#  url = "http://central.maven.org/maven2/",
#)
#
#maven_jar(
#    name = "org_apache_kafka_client",
#    artifact = "org.apache.kafka:kafka-clients:1.0.0",
#)
#maven_jar(
#    name = "org_apache_kafka",
#    artifact = "org.apache.kafka:kafka_2.12:1.0.0",
#)
#
#maven_jar(
#    name = "org_lz4",
#    artifact = "org.lz4:lz4-java:1.4.1"
#)
#maven_jar(
#    name = "org_slf4j",
#    artifact = "org.slf4j:slf4j-api:1.7.25"
#)
#maven_jar(
#    name = "org_xerial_snappy",
#    artifact = "org.xerial.snappy:snappy-java:1.1.7.1"
#)
#
#http_archive(
#	name = "trans_maven_jar",
#	url = "https://github.com/bazelbuild/migration-tooling/archive/master.zip",
#	type = "zip",
#	strip_prefix = "migration-tooling-master",
#)
#
#load("@trans_maven_jar//transitive_maven_jar:transitive_maven_jar.bzl", "transitive_maven_jar")
#
#transitive_maven_jar(
#	name = "dependencies",
#	artifacts = [
#		"rg.lz4:lz4-java:1.4.1",
#	]
#)
#
#load("@dependencies//:generate_workspace.bzl", "generated_maven_jars")
#generated_maven_jars()

load("//:generate_workspace.bzl", "generated_maven_jars")

maven_jar(
    name = "org_slf4j_slf4j_simple",
    artifact = "org.slf4j:slf4j-simple:1.7.25",
)

generated_maven_jars()
