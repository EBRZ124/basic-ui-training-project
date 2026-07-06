# Shelter Starter

## Purpose

This project is a simple sample application for learning basic UI development with Java and Spring Boot. It is intended for coursework, experimentation, and local practice.

## Author

Created and maintained by Elina Rostoka, with development assistance from GitHub Copilot.

## Disclaimer

This repository contains a sample homework project created for an educational course on basic UI development. It is shared for learning purposes, does not represent an official employer product or service, and does not contain proprietary or confidential materials.

## Animal Images

The project supports local image files from static resources.

- Add custom animal photos to `src/main/resources/static/images/animals/`
- Store only the filename in the animal field, for example `luna.jpg`
- The app resolves that value to `/images/animals/luna.jpg`

If image filename/url is missing, a fallback is selected by animal type:

- CAT -> `/images/fallback/fallback-cat.jpg`
- DOG -> `/images/fallback/fallback-dog.jpg`
- OTHER -> `/images/fallback/fallback-other.jpg`

Place fallback files in `src/main/resources/static/images/fallback/`.