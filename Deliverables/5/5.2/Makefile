default: ../../src
	cd `pwd`/../..; \
	cp `pwd`/../Lib/json-simple-1.1.1.jar `pwd`/5/5.2; \
	javac -cp `pwd`/5/5.2/json-simple-1.1.1.jar -g `pwd`/src/edu/northwestern/threeninethree/backgammon/*.java -d `pwd`/5/5.2/out; \
	jar cvmf `pwd`/5/5.2/MANIFEST.MF `pwd`/5/5.2/run.jar -C 5/5.2/out .;

clean:
	$(RM) -r out/*
	$(RM) *.jar