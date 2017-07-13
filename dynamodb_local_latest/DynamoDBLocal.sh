#!/bin/bash -xv

cd `dirname $0`

java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
