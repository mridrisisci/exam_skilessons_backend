package app.dao;

import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class HotelDAO extends GenericDAO implements IHotelDAO
{
    public HotelDAO(EntityManagerFactory emf)
    {
        super(emf);
    }

    public List<Hotel> getAllHotels()
    {
        return super.getAll(Hotel.class);
    }

    public Hotel getHotelById(Long id)
    {
        return super.getById(Hotel.class, id);
    }

    public Hotel createHotel(Hotel hotel)
    {
        return super.create(hotel);
    }

    public Hotel updateEntity(Hotel hotel)
    {
        return super.update(hotel);
    }

    public void deleteHotel(Long id)
    {
        super.delete(Hotel.class, id);
    }

    @Override
    public Hotel addRoom(Hotel hotel, Room room)
    {
        hotel.addRoom(room);
        return update(hotel);
    }

    @Override
    public Hotel removeRoom(Hotel hotel, Room room)
    {
        hotel.removeRoom(room);
        Hotel updatedEntity = update(hotel);
        delete(room);
        return updatedEntity;
    }

    @Override
    public List<Room> getRoomsForHotel(Hotel hotel)
    {
        return hotel.getRooms();
    }
}
