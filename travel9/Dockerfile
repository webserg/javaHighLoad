FROM alpine:3.6

# install bash
RUN \
  apk add --no-cache bash

# install zip
RUN \
  apk add --no-cache zip

RUN \
  mkdir /tmp/hightload
WORKDIR /tmp/hightload
RUN \
  mkdir /tmp/data

COPY ./jdk-9 ./jdk-9

ADD ./target/webserver.jar ./webserver.jar
ADD ./target/test.log ./test.log
ADD ./target/audit.log ./audit.log
ADD ./warmUp.sh ./warmUp.sh
#COPY ./data.zip /tmp/data
RUN \
  mkdir /tmp/unzipped && \
  export DATA_HOME=/tmp/unzipped
EXPOSE 80

CMD \
    unzip /tmp/data/data.zip -d /tmp/unzipped > out.log 2> err.log && \
    ./jdk-9/bin/java -server -Xms3488m -Xmx3488m  -XX:+UseParallelGC -XX:NewSize=1300m -XX:MaxNewSize=1300m -XX:MaxDirectMemorySize=512m -XX:MaxMetaspaceSize=48m -XX:CompressedClassSpaceSize=48m -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Djava.lang.Integer.IntegerCache.high=11000000  -jar ./webserver.jar && \
    ./warmUp.sh

