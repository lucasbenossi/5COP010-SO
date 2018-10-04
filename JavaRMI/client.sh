#!/bin/bash

if ! [[ $1 ]]; then
	echo "Uso: ./client ARQUIVO SERVER1:PORTA [SERVER2:PORTA ...]"
	exit 0
fi

java -cp bin/ so.rmi.Client $@