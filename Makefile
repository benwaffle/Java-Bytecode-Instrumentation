asm = $(wildcard lib/*.jar)
src = $(wildcard src/*.java)

build:
	javac -d obj -cp $(asm) $(src)
run:
	java -cp obj:$(asm) Test
