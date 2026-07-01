package producto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductoTest {

    // =========================================================================
    // TESTS DE ATRIBUTO DINÁMICO
    // =========================================================================

    @Test
    void testAtributoDinamicoValido() {
        AtributoDinamico attr = new AtributoDinamico("Color", "Rojo");
        assertTrue(attr.esValido(), "El atributo debería ser válido");
    }

    @Test
    void testAtributoDinamicoInvalidoPorNombreVacio() {
        AtributoDinamico attr = new AtributoDinamico("", "Rojo");
        assertFalse(attr.esValido(), "El atributo no debe ser válido si el nombre está vacío");
    }

    // =========================================================================
    // TESTS DE PRODUCTO BASE (HOJAS)
    // =========================================================================

    @Test
    void testProductoBaseValidacionExitosa() {
        ProductoBase termo = new ProductoBase(123, "Termo", "Acero inox", "Stanley", "Bazar", 5000.0, 10, 50);
        termo.agregarAtributo("Color", "Verde");
        
        // No debería lanzar excepción
        assertDoesNotThrow(() -> termo.validarItem());
        assertTrue(termo.esItemValido());
    }

    @Test
    void testProductoBaseValidacionFallaPorSKUInvalido() {
        // SKU = 0, lo cual es inválido
        ProductoBase termo = new ProductoBase(0, "Termo", "Acero", "Stanley", "Bazar", 5000.0, 0, 50);
        
        assertFalse(termo.esItemValido());
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            termo.validarItem();
        });
        assertEquals("No es un producto válido", exception.getMessage());
    }

    @Test
    void testProductoBaseCalculaPrecioFinal() {
        // Precio: 1000, Descuento: 20% -> Precio Final: 800
        ProductoBase prod = new ProductoBase(1, "Prod", "Desc", "Marca", "Cat", 1000.0, 20, 10);
        
        
        assertEquals(800.0, prod.precioFinal(), 0.01); 
    }

    // =========================================================================
    // TESTS DE PAQUETE (COMPOSITE)
    // =========================================================================

    @Test
    void testPaqueteSumaPrecioBaseDeSusProductos() {
        ProductoBase p1 = new ProductoBase(1, "P1", "-", "M1", "C1", 1000.0, 0, 10);
        ProductoBase p2 = new ProductoBase(2, "P2", "-", "M2", "C1", 2000.0, 0, 10);
        
        Paquete paquete = new Paquete("Combo", "Desc", "Cat", 0.0, 0, 5);
        paquete.agregarProducto(p1);
        paquete.agregarProducto(p2);
        
        assertEquals(3000.0, paquete.getPrecioBase(), 0.01);
    }

    @Test
    void testPaqueteCalculaPrecioFinalConDescuento() {
        ProductoBase p1 = new ProductoBase(1, "P1", "-", "M1", "C1", 1000.0, 0, 10);
        ProductoBase p2 = new ProductoBase(2, "P2", "-", "M2", "C1", 2000.0, 0, 10);
        
        // Paquete con 10% de descuento
        Paquete paquete = new Paquete("Combo", "Desc", "Cat", 0.0, 10, 5);
        paquete.agregarProducto(p1);
        paquete.agregarProducto(p2);
        
        // Base = 3000. Menos el 10% (300) = 2700
        assertEquals(2700.0, paquete.precioFinal(), 0.01);
    }
}