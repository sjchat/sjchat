#!/bin/bash
read -p "DANGER: This will remove *all* docker images on your system. Are you sure? (y/n) " -n 1 -r
echo    # (optional) move to a new line
if [[ $REPLY =~ ^[Yy]$ ]]
then
    # Delete all containers
    echo "Deleting containers..."
    docker rm $(docker ps -a -q)
    # Delete all images
    echo "Deleting images..."
    docker rmi $(docker images -q)
else #
    echo "Did nothing"
fi
