=====================================================
    NEON STEED - GUÍA DE DESARROLLO
=====================================================

El motor base del juego ya está funcionando (físicas, colisiones escaladas y entidades).
He dejado el código organizado para que puedas seguir con la parte estética y de pulido.

-----------------------------------------------------
📍 TAREAS PRIORITARIAS (POR HACER)
-----------------------------------------------------

1. IMPLEMENTAR FONDO REAL (PARALLAX)
   - Actualmente el cielo es un color sólido (canvas.drawColor).
   - FALTA: Añadir una o dos capas de fondo que se muevan a distintas velocidades.
   - RECOMENDACIÓN: Crear una clase 'CapaFondo.kt' que reciba un Bitmap y una
     velocidad relativa (ej. speed * 0.3 para montañas lejanas).

2. AJUSTE DE TAMAÑOS Y PROPORCIONES
   - He implementado un sistema de 'baseScale' en GameView.kt para que el juego
     no se vea pequeño en pantallas de alta resolución.
   - TAREA: Revisa los tamaños en generarObstaculo(). Si los sprites se ven
     deformes o el hit-box (caja de colisión) es muy injusto, ajusta los
     valores de 'w' y 'h' multiplicados por 'baseScale'.

3. MEJORAR EL "GAME JUICE"
   - Añadir partículas de polvo (círculos color arena) cuando el caballo
     esté tocando el suelo.

4. 🔊 SONIDO
   - El juego está en silencio. Hay que implementar MediaPlayer para la música
     de fondo y SoundPool para los efectos de salto y choque.

-----------------------------------------------------
📂 ESTRUCTURA DE ARCHIVOS
-----------------------------------------------------
- GameView.kt: El cerebro. Gestiona el bucle, la escala y el dibujado.
- entities/:
    - GameObject.kt: Clase madre con la lógica de RectF.
    - Caballito.kt: Lógica de salto y gravedad.
    - Obstaculo.kt: Gestión de cactus, buitres y estalactitas. (rETOCAR TAMAÑOS, TIEMPO EN EL QUE APARECEN ETC)
    - Fondo.kt: Movimiento del suelo.

- res/drawable/:
    - Aquí están los sprites (caballito, cactus, buitre, estalactita).
    - OJO: Los nombres deben estar siempre en minúsculas.

-----------------------------------------------------
⚙️ NOTAS TÉCNICAS
-----------------------------------------------------
- Si quieres cambiar la dificultad, toca 'currentSpeed' y 'currentInterval'
  en el método update() de GameView.
- El sistema de niveles cambia el color del cielo automáticamente cada 15 puntos. (NO FUNCIONA)

=====================================================
