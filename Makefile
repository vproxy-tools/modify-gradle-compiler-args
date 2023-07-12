.PHONY: all
all: jar install

.PHONY: clean
clean:
	./gradlew clean

.PHONY: jar
jar:
	./gradlew jar

.PHONY: install
install: jar
	cp agent/build/libs/modify-gradle-compiler-args-agent.jar ~/.gradle/modify-gradle-compiler-args-agent.jar
