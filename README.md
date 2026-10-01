# Simple Android Game

Game sederhana untuk Android dengan konsep `Catch the Ball`.

Fitur:
- Bola jatuh dari atas layar
- Pemain menggerakkan paddle ke kiri dan kanan dengan sentuhan layar
- Skor bertambah saat bola tertangkap
- Nyawa berkurang saat bola terlewat
- Tombol mulai / restart

## Cara menjalankan
1. Buka project ini di Android Studio.
2. Tunggu proses sync Gradle selesai.
3. Pilih emulator atau device Android.
4. Tekan Run.

## Struktur utama
- `app/src/main/java/com/example/simpleandroidgame/MainActivity.kt` : Activity utama
- `app/src/main/java/com/example/simpleandroidgame/GameView.kt` : logika game
- `app/src/main/res/layout/activity_main.xml` : layout UI

## Catatan
Aplikasi ini dibuat dengan Kotlin dan XML sederhana agar mudah dimodifikasi.
