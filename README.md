# LocationFinder App

## Features

- **Add Location**: Add new locations with address, latitude, and longitude. IDs are assigned automatically.
- **Query Location**: Retrieve latitude and longitude by entering an address.
- **Update Location**: Update latitude and longitude for an existing address.
- **Delete Location**: Delete a location with a confirmation dialog.

## Architecture

- **Client-Server**: Android client interacts with Firebase Realtime Database for CRUD operations.
- **Database Structure**:
  - `id`: Consecutive identifier for each location.
  - `address`: Location name (e.g., "Oshawa").
  - `latitude`: Latitude of the location.
  - `longitude`: Longitude of the location.

## Design 

- **Modular Code**: Methods are organized by function (add, query, update, delete).
- **Material Design UI**: Uses collapsible card views for a clean, organized layout.
- **Error Handling**: User-friendly messages and real-time data updates.

![image](https://github.com/user-attachments/assets/7af3205a-6163-4d33-90da-f34c5f6a26a0)

