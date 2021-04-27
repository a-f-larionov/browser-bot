#pgrep java | xargs kill
#pgrep chrome | xargs kill

#mvn clean

#mvn -Dtests.skip=true package

while true; do

  mvn -T 1C clean install -Dmaven.test.skip -DskipTests -Dmaven.javadoc.skip=true

  pkill java

  pkill chrome

  java -jar -Dspring.profiles.active=prod target/taplinkbot-0.0.1-SNAPSHOT.jar >>java.log

done
