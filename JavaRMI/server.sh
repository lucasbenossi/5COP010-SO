#!/bin/bash

if ! [[ $1 ]]; then
	echo "Uso: ./server PORTA"
	exit 0
fi

java -cp bin/ so.rmi.Server $1