#!/bin/bash

if ! [[ $1 ]]; then
	echo "Uso: ./server PORTA"
	exit 0
fi

java -cp bin/ -Djava.rmi.server.hostname=191.52.64.50 so.rmi.Server $1
