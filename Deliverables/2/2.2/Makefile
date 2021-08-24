default: ../../src
	cd `pwd`/../..; \
	cp `pwd`/../Lib/json-simple-1.1.1.jar `pwd`/2/2.2; \
	javac -cp `pwd`/2/2.2/json-simple-1.1.1.jar -g `pwd`/src/edu/northwestern/threeninethree/backgammon/Backend.java `pwd`/src/edu/northwestern/threeninethree/backgammon/Frontend.java -d `pwd`/2/2.2/out; \
	jar cvmf `pwd`/2/2.2/MANIFEST.MF `pwd`/2/2.2/run.jar -C 2/2.2/out .;

clean:
	$(RM) -r out/*
	$(RM) *.jar