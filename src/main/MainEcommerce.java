package main;

import ecommerce.Ecommerce;
import envio.EnvioEstandar;
import envio.EnvioExpress;
import envio.RetiroSucursal;
import envio.Sucursal;
import envio.LibEnvioExpress;
import metodoDePago.*;
import pedido.*;
import producto.*;
import criterio.*;
import visitor.*;
import envio.CorreoArgentino;

import java.time.LocalDate;
import java.util.*;

/**
 * Flujo completo de Ecommerce para demostrar su funcionalidad.
 */
public class MainEcommerce {

    public static void main(String[] args) {
        System.out.println("🚀 INICIANDO FLUJO COMPLETO DEL ECOMMERCE\n");
        System.out.println("═══════════════════════════════════════════════════\n");

        // ============================================================
        // 1. CREAR ECOMMERCE
        // ============================================================
        Ecommerce ecommerce = new Ecommerce();
        System.out.println("✅ Ecommerce creado");

        // ============================================================
        // 2. CREAR PRODUCTOS
        // ============================================================
        System.out.println("\n📦 CREANDO PRODUCTOS...");

        Producto laptop = new ProductoBase(
            1001, "Laptop Gamer", "Laptop de alta gama", "Asus", "Electronica", 1200.0, 10, 15
        );
        
        Producto mouse = new ProductoBase(
            1002, "Mouse Inalámbrico", "Mouse ergonómico", "Logitech", "Electronica", 50.0, 5, 30
        );
        
        Producto teclado = new ProductoBase(
            1003, "Teclado Mecánico", "Teclado RGB", "Redragon", "Electronica", 80.0, 0, 20
        );
        
        Producto silla = new ProductoBase(
            1004, "Silla Gamer", "Silla ergonómica", "Corsair", "Hogar", 300.0, 15, 5
        );

        // Agregar productos al catálogo
        ecommerce.agregarProductoACatalogo(laptop);
        ecommerce.agregarProductoACatalogo(mouse);
        ecommerce.agregarProductoACatalogo(teclado);
        ecommerce.agregarProductoACatalogo(silla);
        System.out.println("✅ 4 productos agregados al catálogo");

        // ============================================================
        // 3. CREAR MÉTODOS DE ENVÍO
        // ============================================================
        System.out.println("\n📦 CREANDO MÉTODOS DE ENVÍO...");

        // Correo Argentino (para envío estándar)
        CorreoArgentino correoArgentino = (peso, destino) -> {
            return peso * 10.0 + 50.0; // Costo: $50 fijo + $10 por kg
        };

        // Envío Estándar
        EnvioEstandar envioEstandar = new EnvioEstandar(correoArgentino);

        // Envío Express
        LibEnvioExpress libExpress = (precio) -> {
            return precio * 0.10; // 10% del valor total
        };
        EnvioExpress envioExpress = new EnvioExpress(libExpress);

        // Retiro en Sucursal
        Sucursal sucursal = new Sucursal();
        RetiroSucursal retiroSucursal = new RetiroSucursal(sucursal);

        System.out.println("✅ 3 métodos de envío creados: Estándar, Express, Retiro en Sucursal");

        // ============================================================
        // 4. CREAR MÉTODOS DE PAGO (Con mocks de APIs)
        // ============================================================
        System.out.println("\n💳 CREANDO MÉTODOS DE PAGO...");

        // API Tarjeta de Crédito (mock simple)
        ApiTarjetaCredito apiTarjeta = new ApiTarjetaCredito() {
            @Override
            public boolean validarTarjeta(String numero, String cvv, LocalDate vencimiento) {
                return true; // Siempre válida para el demo
            }
            @Override
            public void preAutorizar(double monto) {
                System.out.println("   💳 Pre-autorización de $" + monto);
            }
            @Override
            public String transferir(double monto) {
                String codigo = "TXN-" + System.currentTimeMillis();
                System.out.println("   💳 Transferencia de $" + monto + " - Código: " + codigo);
                return codigo;
            }
        };

        // API Transferencia Bancaria (mock simple)
        ApiTransferenciaBancaria apiTransferencia = new ApiTransferenciaBancaria() {
            @Override
            public boolean validarCuenta(String cbu, String alias) {
                return true;
            }
            @Override
            public String transferir(double monto) {
                String codigo = "TRX-" + System.currentTimeMillis();
                System.out.println("   🏦 Transferencia bancaria de $" + monto + " - Código: " + codigo);
                return codigo;
            }
        };

        // API Billetera Virtual (mock simple)
        ApiBilleteraVirtual apiBilletera = new ApiBilleteraVirtual() {
            private double saldo = 5000.0;
            @Override
            public boolean tieneSaldo(double monto) {
                return saldo >= monto;
            }
            @Override
            public void bloquearSaldo(double monto) {
                saldo -= monto;
                System.out.println("   📱 Saldo bloqueado: $" + monto + " - Saldo restante: $" + saldo);
            }
            @Override
            public String acreditar(double monto) {
                String codigo = "BVT-" + System.currentTimeMillis();
                System.out.println("   📱 Acreditación de $" + monto + " - Código: " + codigo);
                return codigo;
            }
        };

        // Crear métodos de pago
        MetodoDePago pagoTarjeta = new TarjetaDeCredito(0, "1234567890123456", "123", LocalDate.of(2026, 12, 31), apiTarjeta);
        MetodoDePago pagoTransferencia = new TransferenciaBancaria(0, "CBU1234567890", "alias123", apiTransferencia);
        MetodoDePago pagoBilletera = new BilleteraVirtual(0, apiBilletera);

        System.out.println("✅ 3 métodos de pago creados: Tarjeta, Transferencia, Billetera Virtual");

        // ============================================================
        // 5. CREAR OBSERVADORES (Notificaciones)
        // ============================================================
        System.out.println("\n📧 CREANDO OBSERVADORES...");

        // MailSender mock
        NotificadorEmail.MailSender mailSender = (direccion, titulo, mensaje, adjunto) -> {
            System.out.println("   📧 EMAIL ENVIADO");
            System.out.println("   📧 Para: " + direccion);
            System.out.println("   📧 Título: " + titulo);
            System.out.println("   📧 Mensaje: " + mensaje);
        };

        ObservadorPedido notificadorEmail = new NotificadorEmail(mailSender);
        ObservadorPedido generadorFactura = new GeneradorFactura();
        ObservadorPedido fidelizacion = new Fidelizacion();

        System.out.println("✅ 3 observadores creados: Email, Factura, Fidelización");

        // ============================================================
        // 6. CREAR Y PROCESAR PEDIDOS
        // ============================================================
        System.out.println("\n🛒 CREANDO PEDIDOS...");

        // ===== PEDIDO 1: Productos electrónicos con envío estándar y pago con tarjeta =====
        System.out.println("\n--- PEDIDO 1: Laptop + Mouse | Envío Estándar | Pago con Tarjeta ---");
        
        List<Producto> productosPedido1 = Arrays.asList(laptop, mouse);
        Pedido pedido1 = new Pedido("Calle Falsa 123, Buenos Aires", productosPedido1, envioEstandar, pagoTarjeta);
        pedido1.agregarObserver(notificadorEmail);
        pedido1.agregarObserver(generadorFactura);
        ecommerce.agregarPedido(pedido1);
        
        System.out.println("📋 Pedido 1 creado");
        System.out.println("   💰 Total: $" + pedido1.valorTotal());
        System.out.println("   📦 Peso: " + pedido1.getPesoTotal() + " kg");
        System.out.println("   🚚 Costo envío: $" + envioEstandar.calcularCostoEnvio(pedido1));
        System.out.println("   📅 Entrega estimada: " + envioEstandar.calcularEntrega(pedido1));

        // Procesar el pedido (flujo completo)
        pedido1.confirmar();    // Borrador -> Confirmado (procesa pago)
        pedido1.preparar();     // Confirmado -> Preparacion (decrementa stock)
        pedido1.enviar();       // Preparacion -> Enviado
        pedido1.entregar();     // Enviado -> Entregado (genera factura)

        // ===== PEDIDO 2: Teclado + Silla | Envío Express | Pago con Transferencia =====
        System.out.println("\n--- PEDIDO 2: Teclado + Silla | Envío Express | Pago con Transferencia ---");
        
        List<Producto> productosPedido2 = Arrays.asList(teclado, silla);
        Pedido pedido2 = new Pedido("Av. Siempreviva 742, Springfiled", productosPedido2, envioExpress, pagoTransferencia);
        pedido2.agregarObserver(notificadorEmail);
        ecommerce.agregarPedido(pedido2);
        
        System.out.println("📋 Pedido 2 creado");
        System.out.println("   💰 Total: $" + pedido2.valorTotal());
        System.out.println("   🚚 Costo envío: $" + envioExpress.calcularCostoEnvio(pedido2));
        System.out.println("   📅 Entrega estimada: " + envioExpress.calcularEntrega(pedido2));

        pedido2.confirmar();
        pedido2.preparar();
        pedido2.enviar();

        // ===== PEDIDO 3: Retiro en Sucursal | Pago con Billetera Virtual =====
        System.out.println("\n--- PEDIDO 3: Laptop + Teclado | Retiro en Sucursal | Pago con Billetera Virtual ---");
        
        List<Producto> productosPedido3 = Arrays.asList(laptop, teclado);
        Pedido pedido3 = new Pedido("Sucursal Centro", productosPedido3, retiroSucursal, pagoBilletera);
        pedido3.agregarObserver(notificadorEmail);
        pedido3.agregarObserver(fidelizacion);
        ecommerce.agregarPedido(pedido3);
        
        System.out.println("📋 Pedido 3 creado");
        System.out.println("   💰 Total: $" + pedido3.valorTotal());
        System.out.println("   🚚 Costo envío: $" + retiroSucursal.calcularCostoEnvio(pedido3));
        System.out.println("   📅 Entrega estimada: " + retiroSucursal.calcularEntrega(pedido3));

        pedido3.confirmar();
        pedido3.preparar();

        // ============================================================
        // 7. BUSCAR PRODUCTOS (Demostración de Criterios)
        // ============================================================
        System.out.println("\n🔍 BUSCANDO PRODUCTOS...");

        // Buscar por categoría
        Criterio criterioCategoria = new CriterioPorCategoria("Electronica");
        List<Producto> electronicos = ecommerce.buscarProducto(criterioCategoria);
        System.out.println("📋 Productos en categoría 'Electronica': " + electronicos.size() + " encontrados");
        electronicos.forEach(p -> System.out.println("   - " + p.getNombre()));

        // Buscar por nombre
        Criterio criterioNombre = new CriterioPorNombre("Gamer");
        List<Producto> productosGamer = ecommerce.buscarProducto(criterioNombre);
        System.out.println("\n📋 Productos que contienen 'Gamer': " + productosGamer.size() + " encontrados");
        productosGamer.forEach(p -> System.out.println("   - " + p.getNombre()));

        // ============================================================
        // 8. GENERAR REPORTE DE VENTAS
        // ============================================================
        System.out.println("\n📊 GENERANDO REPORTE DE VENTAS...");

        LocalDate desde = LocalDate.now().minusDays(7);
        LocalDate hasta = LocalDate.now();

        System.out.println("\n--- REPORTE EN CSV ---");
        ReporteVisitor reporteCSV = new ReporteCSV();
        String csv = ecommerce.generarReporte(desde, hasta, reporteCSV);
        System.out.println(csv);

        System.out.println("\n--- REPORTE EN TXT ---");
        ReporteVisitor reporteTXT = new ReporteTXT();
        String txt = ecommerce.generarReporte(desde, hasta, reporteTXT);
        System.out.println(txt);

        System.out.println("\n--- REPORTE EN HTML ---");
        ReporteVisitor reporteHTML = new ReporteHTML();
        String html = ecommerce.generarReporte(desde, hasta, reporteHTML);
        System.out.println("<table>");
        System.out.println(html);

        // ============================================================
        // 9. RESUMEN FINAL
        // ============================================================
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("📊 RESUMEN FINAL");
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("✅ Total de productos en catálogo: " + electronicos.size());
        System.out.println("✅ Total de pedidos creados: 3");
        System.out.println("✅ Estados de los pedidos:");
        System.out.println("   - Pedido 1: " + pedido1.getEstado().getClass().getSimpleName());
        System.out.println("   - Pedido 2: " + pedido2.getEstado().getClass().getSimpleName());
        System.out.println("   - Pedido 3: " + pedido3.getEstado().getClass().getSimpleName());
        System.out.println("✅ Reportes generados: CSV, TXT, HTML");
        System.out.println("\n🚀 FLUJO COMPLETO FINALIZADO CON ÉXITO");
    }
}