package app.dao;

import app.entities.Hotel;
import app.entities.Room;

import java.util.List;

public interface IHotelDAO
{
    Hotel getHotelById(Long id);
    Hotel addRoom(Hotel hotel, Room room);
    Hotel removeRoom(Hotel hotel, Room room);
    List<Room> getRoomsForHotel(Hotel hotel);
}
