# 🔐 Informasi Akses Pengguna MediCord

## 📋 Daftar Pengguna yang Dapat Mengakses Aplikasi

### 👤 User Default (Otomatis Dibuat)

Saat aplikasi pertama kali dijalankan, sistem akan **otomatis membuat** satu user default jika belum ada user di database:

| Username | Password | Role | Keterangan |
|----------|----------|------|------------|
| **admin** | **admin123** | Admin | User default yang dibuat otomatis |

---

## 🔍 Detail Sistem Autentikasi

### Cara Kerja Login

1. **Validasi Input:**
   - Username dan password harus diisi
   - Jika kosong, akan muncul error: "Username dan password harus diisi"

2. **Proses Autentikasi:**
   - Sistem mencari user di database dengan username dan password yang cocok
   - Jika ditemukan → Login berhasil
   - Jika tidak ditemukan → Login gagal

3. **Keamanan Login:**
   - **Batas percobaan:** Maksimal 3 kali percobaan login
   - Setelah 3 kali gagal → Aplikasi terkunci dengan pesan: "Login gagal 3 kali. Aplikasi terkunci."

### Role yang Tersedia

Aplikasi mendukung 2 jenis role:

1. **Admin** - Administrator dengan akses penuh
2. **Petugas** - Petugas dengan akses terbatas

> **Catatan:** Saat ini, aplikasi belum membedakan akses berdasarkan role. Semua user yang login memiliki akses yang sama.

---

## ➕ Menambah User Baru

### Melalui Kode (Programmatically)

User baru dapat ditambahkan melalui kode menggunakan `UserRepository` atau `UserDao`:

```kotlin
val userRepository = UserRepository(db.userDao())

// Menambah user baru
userRepository.insertUser(
    UserEntity(
        username = "petugas1",
        password = "petugas123",
        role = "Petugas"
    )
)
```

### Contoh User Tambahan

Jika Anda ingin menambah user tambahan, berikut contohnya:

| Username | Password | Role |
|----------|----------|------|
| petugas1 | petugas123 | Petugas |
| dokter1 | dokter123 | Admin |
| staff1 | staff123 | Petugas |

> **⚠️ PENTING:** User baru harus ditambahkan melalui kode atau langsung ke database, karena **tidak ada UI/fitur di aplikasi untuk menambah user baru**.

---

## 🔎 Cara Melihat Semua User yang Terdaftar

### Status Saat Ini

**Tidak ada fitur di UI aplikasi untuk melihat daftar semua user yang terdaftar.**

### Opsi untuk Melihat User

1. **Melalui Database Browser:**
   - Gunakan aplikasi seperti **DB Browser for SQLite** atau **Android Studio Database Inspector**
   - Buka file database: `medicord_database`
   - Lihat tabel `user`

2. **Menambah Fitur (Perlu Development):**
   - Tambahkan query `getAllUsers()` di `UserDao`
   - Buat screen untuk menampilkan daftar user
   - Tambahkan fitur CRUD untuk user management

---

## 📝 Struktur Data User

### UserEntity

```kotlin
data class UserEntity(
    val id: Long = 0,                    // Auto-generated ID
    val username: String,                // Username untuk login
    val password: String,                 // Password (plain text)
    val role: String = "Admin"           // "Admin" atau "Petugas"
)
```

### Tabel Database: `user`

| Field | Tipe | Deskripsi |
|-------|------|-----------|
| `id` | Long (Primary Key, AutoGenerate) | ID unik user |
| `username` | String | Username untuk login (unique) |
| `password` | String | Password (disimpan plain text) |
| `role` | String | "Admin" atau "Petugas" |

---

## ⚠️ Catatan Keamanan

### Masalah Keamanan yang Perlu Diperhatikan

1. **Password Disimpan Plain Text**
   - ⚠️ Password disimpan tanpa enkripsi/hashing
   - **Rekomendasi:** Gunakan hashing (bcrypt, SHA-256) untuk production

2. **Tidak Ada Fitur User Management**
   - Tidak ada UI untuk menambah/edit/hapus user
   - Tidak ada fitur reset password
   - Tidak ada fitur change password

3. **Tidak Ada Session Management**
   - Tidak ada logout functionality yang jelas
   - Tidak ada token/session management

4. **Tidak Ada Role-Based Access Control**
   - Semua user memiliki akses yang sama
   - Role "Admin" dan "Petugas" belum dibedakan

---

## 🚀 Rekomendasi Pengembangan

### Fitur yang Bisa Ditambahkan

1. **User Management Screen:**
   - Daftar semua user
   - Tambah user baru
   - Edit user
   - Hapus user
   - Reset password

2. **Keamanan:**
   - Hash password dengan bcrypt
   - Implementasi session/token
   - Logout functionality
   - Role-based access control

3. **Fitur Tambahan:**
   - Change password
   - Forgot password
   - Account lockout setelah beberapa percobaan gagal
   - Activity log untuk tracking login

---

## 📍 Lokasi File Terkait

- **User Entity:** `app/src/main/java/com/example/medicord/Data/database/entity/UserEntity.kt`
- **User DAO:** `app/src/main/java/com/example/medicord/Data/database/dao/UserDao.kt`
- **User Repository:** `app/src/main/java/com/example/medicord/Data/repository/UserRepository.kt`
- **Login ViewModel:** `app/src/main/java/com/example/medicord/viewmodel/LoginViewModel.kt`
- **Login Screen:** `app/src/main/java/com/example/medicord/UserInterface/screens/LoginScreen.kt`
- **MainActivity (Default User):** `app/src/main/java/com/example/medicord/MainActivity.kt`

---

## ✅ Kesimpulan

**User yang Dapat Mengakses Aplikasi Saat Ini:**

1. **admin** / **admin123** (Admin) - User default yang dibuat otomatis

**Untuk menambah user baru:**
- Harus dilakukan melalui kode atau langsung ke database
- Tidak ada UI untuk menambah user

**Untuk melihat semua user:**
- Gunakan database browser atau Android Studio Database Inspector
- Tidak ada fitur di UI aplikasi

---

**Terakhir Diupdate:** Berdasarkan analisis kode proyek MediCord
