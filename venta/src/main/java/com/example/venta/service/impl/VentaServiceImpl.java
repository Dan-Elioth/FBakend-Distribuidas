package com.example.venta.service.impl;

import com.example.venta.dto.Cliente;
import com.example.venta.dto.Producto;
import com.example.venta.entity.Venta;
import com.example.venta.entity.VentaDetalle;
import com.example.venta.feign.ClienteFeign;
import com.example.venta.feign.ProductoFeign;
import com.example.venta.repository.VentaRepository;
import com.example.venta.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteFeign clienteFeign;

    @Autowired
    private ProductoFeign productoFeign;

    @Override
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta guardar(Venta Venta) {
        return ventaRepository.save(Venta);
    }

    @Override
    public Venta actualizar(Venta Venta) {
        return ventaRepository.save(Venta);
    }

    @Override
    public Optional<Venta> listarPorId(Integer id) {
        Venta venta = ventaRepository.findById(id).get();

        Cliente cliente = clienteFeign.listById(venta.getClienteId()).getBody();
        List<VentaDetalle> ventaDetalles = venta.getDetalle().stream().map(ventaDetalle -> {
            System.out.println(ventaDetalle.toString());
            System.out.println("Antes de la peticion");
            Producto producto = productoFeign.listById(ventaDetalle.getProductoId()).getBody();
            System.out.println("Despues de la peticion");
            System.out.println(producto.toString());
            System.out.println(producto.getNombre());
            ventaDetalle.setProducto(producto);
            return ventaDetalle;
        }).collect(Collectors.toList());
        venta.setDetalle(ventaDetalles);

        venta.setCliente(cliente);
        return Optional.of(venta);
    }

    @Override
    public void eliminarPorId(Integer id) {
        ventaRepository.deleteById(id);
    }
}