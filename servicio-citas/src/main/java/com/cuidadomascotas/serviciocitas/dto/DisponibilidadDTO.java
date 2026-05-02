package com.cuidadomascotas.serviciocitas.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DisponibilidadDTO {

    private LocalDate fecha;
    private List<LocalTime> horariosDisponibles;
    private List<LocalTime> horariosOcupados;
    private int totalDisponibles;

    public DisponibilidadDTO() {
    }

    public DisponibilidadDTO(LocalDate fecha, List<LocalTime> horariosDisponibles, List<LocalTime> horariosOcupados) {
        this.fecha = fecha;
        this.horariosDisponibles = horariosDisponibles;
        this.horariosOcupados = horariosOcupados;
        this.totalDisponibles = horariosDisponibles.size();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<LocalTime> getHorariosDisponibles() {
        return horariosDisponibles;
    }

    public void setHorariosDisponibles(List<LocalTime> horariosDisponibles) {
        this.horariosDisponibles = horariosDisponibles;
        this.totalDisponibles = horariosDisponibles == null ? 0 : horariosDisponibles.size();
    }

    public List<LocalTime> getHorariosOcupados() {
        return horariosOcupados;
    }

    public void setHorariosOcupados(List<LocalTime> horariosOcupados) {
        this.horariosOcupados = horariosOcupados;
    }

    public int getTotalDisponibles() {
        return totalDisponibles;
    }

    public void setTotalDisponibles(int totalDisponibles) {
        this.totalDisponibles = totalDisponibles;
    }
}
