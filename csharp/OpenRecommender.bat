@echo off

echo creating object bindings to schemas
xsd OpenRecommender.xsd /classes /namespace:OpenRecommender

echo building program to demonstrate xml serialization
csc /out:OpenRecommender.exe /target:exe index.cs OpenRecommender.cs

echo -- running OpenRecommender.exe
OpenRecommender
