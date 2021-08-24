default: ../../src
	cd `pwd`/../..; \
	cp `pwd`/../Lib/json-simple-1.1.1.jar `pwd`/8/8.1; \
	javac -cp `pwd`/8/8.1/json-simple-1.1.1.jar -g `pwd`/src/edu/northwestern/threeninethree/backgammon/*.java -d `pwd`/8/8.1/out; \
	jar cvmf `pwd`/8/8.1/MANIFEST.MF `pwd`/8/8.1/run.jar -C 8/8.1/out .;
	jar cvmf `pwd`/MANIFEST2.MF `pwd`/run2.jar -C out .;

clean:
	$(RM) -r out/*
	$(RM) *.jar