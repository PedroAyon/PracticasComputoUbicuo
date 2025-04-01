if [ -s compileerror.txt ]
then
echo "no se pudo llevar acabo"
else
	if [-f -s error.txt]
		then
		echo "error a la hora de exportar varibale mpi" 2>error.txt
	else
export PATH="$MSMPI_BIN":$PATH
mpiexec -n 4 a.exe 1>salida.txt
	fi
fi