package producto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import producto.Paquete;
import producto.ProductoBase;

class ProductoTest {

	private ProductoBase auriculares;
    private ProductoBase funda;
    private ProductoBase cable;

    @BeforeEach
    public void setUp() {
        // Inicializamos los productos individuales con los datos exactos de la tabla
        // Parámetros: SKU, nombre, descripcion, marca, categoria, precio, stock
        auriculares = new ProductoBase(101, "Auriculares Bluetooth", "Alta fidelidad", "Sony","Audio", 8000.0, 50);
        funda = new ProductoBase(102, "Funda protectora", "Silicona reforzada", "Generic", "Accesorios", 1500.0, 100);
        cable = new ProductoBase(103, "Cable USB-C", "Carga rápida 1m", "Anker", "Cables", 800.0, 200);
    }

    @Test
    public void testPrecioFinalProductoIndividualSinDescuento() {
        // Un producto base debe retornar su propio precioBase como precio final
        assertEquals(8000.0, auriculares.precioFinal(), 0.01);
        assertEquals(1500.0, funda.precioFinal(), 0.01);
        assertEquals(800.0, cable.precioFinal(), 0.01);
    }

    @Test
    public void testPackAudioMovilSimple() {
        // Armamos el "Pack Audio Móvil" de la tabla
        // Parámetros: nombre, descripcion, descuento, categoria, precio (pasamos 0.0), stock
        Paquete packAudio = new Paquete("Pack Audio Móvil", "Combo ideal para viajes", 15, "Audio", 0.0, 10);
        
        packAudio.getProductos().add(auriculares);
        packAudio.getProductos().add(funda);
        packAudio.getProductos().add(cable);

        // 1. El precio base del paquete debe ser la suma de sus componentes (8000 + 1500 + 800 = 10300)
        assertEquals(10300.0, packAudio.getPrecioBase(), 0.01);
        
        // 2. El precio final debe aplicar el 15% de descuento sobre esa suma (10300 * 0.85 = 8755)
        assertEquals(8755.0, packAudio.precioFinal(), 0.01);
    }

    @Test
    public void testPaquetesAnidadosRecursivos() {
        // 1. Recreamos el Pack Audio Móvil (Precio Final: 8755)
        Paquete packAudio = new Paquete("Pack Audio Móvil", "Combo de audio", 15, "Audio", 0.0, 10);
        packAudio.getProductos().add(auriculares);
        packAudio.getProductos().add(funda);
        packAudio.getProductos().add(cable);

        // 2. Creamos un producto extra: un Mouse de $2.000
        ProductoBase mouse = new ProductoBase(104, "Mouse Inalámbrico", "Ergonómico", "Logitech", "Periféricos", 2000.0, 30);

        // 3. Creamos el súper combo: "Kit Home Office" con un 10% de descuento
        Paquete kitHomeOffice = new Paquete("Kit Home Office", "Todo para trabajar desde casa", 10, "Oficina", 0.0, 5);
        
        // Metemos el paquete existente y el mouse (Anidación del Composite)
        kitHomeOffice.getProductos().add(packAudio); 
        kitHomeOffice.getProductos().add(mouse);

        // Nota: Como tu método getPrecioBase() actual en Paquete suma 'p.getPrecioBase()',
        // el precio base del Kit sumará el precioBase de packAudio (que es 10300) + mouse (2000) = 12300
        assertEquals(12300.0, kitHomeOffice.getPrecioBase(), 0.01);
        
        // Precio Final del Kit aplicando su 10% de descuento: 12300 * 0.90 = 11070
        assertEquals(11070.0, kitHomeOffice.precioFinal(), 0.01);
    }

    @Test
    public void testProductoBasePropiedades() {
        // Verificamos que los getters devuelvan los valores inicializados del constructor
        assertEquals(101, auriculares.getSKU());
        assertEquals("Sony", auriculares.getMarca());
        assertEquals("Audio", auriculares.getCategoria());
        assertEquals(50, auriculares.getStock());
        assertEquals("Auriculares Bluetooth", auriculares.getNombre());
    }
}