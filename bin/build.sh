#!/usr/bin/env bash

set -a

echo ""
echo "--- Nettoyage de la version précédente de maven..."
mvn clean
echo "--- Nettoyage terminé."
echo ""

echo ""
echo "--- Packaging du projet..."
if [ -f "pom.xml" ]; then
    mvn package
else
    echo ""
    echo "Le fichier pom.xml n'existe pas !"
    exit 1;
fi
echo "--- Packaging du projet terminé."
echo ""



echo ""
echo "|---------------------------------------|"
echo "|----- MISE EN PRODUCTION TERMINÉE -----|"
echo "|---------------------------------------|"
echo ""
