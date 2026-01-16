# 📱 Dokumentasi Proyek MediCord

## 📋 Ringkasan Proyek

**MediCord** adalah aplikasi Android untuk sistem manajemen data pasien yang dibangun menggunakan **Jetpack Compose** dan **Kotlin**. Aplikasi ini dirancang untuk membantu tenaga medis dalam mengelola informasi pasien, riwayat kunjungan, dan data obat secara efisien.

---

## 🏗️ Arsitektur Aplikasi

Aplikasi ini menggunakan arsitektur **MVVM (Model-View-ViewModel)** dengan komponen-komponen berikut:

### 1. **Model Layer** (`Data/model/`)
- Data classes yang merepresentasikan entitas bisnis
- Contoh: `Pasien.kt`, `User.kt`, `HistoriVisit.kt`, `Obat.kt`

### 2. **Entity Layer** (`Data/database/entity/`)
- Room Database entities untuk penyimpanan data
- Entities: `PasienEntity`, `UserEntity`, `HistoriVisitEntity`, `ObatEntity`

### 3. **DAO Layer** (`Data/database/dao/`)
- Data Access Objects untuk operasi database
- DAOs: `PasienDao`, `UserDao`, `HistoriVisitDao`, `ObatDao`

### 4. **Repository Layer** (`Data/repository/`)
- Layer abstraksi antara ViewModel dan Database
- Repositories: `PasienRepository`, `UserRepository`, `HistoriVisitRepository`, `ObatRepository`

### 5. **ViewModel Layer** (`viewmodel/`)
- Mengelola UI state dan logika bisnis
- ViewModels: `PasienViewModel`, `LoginViewModel`, `PasienFormViewModel`, `PasienDetailViewModel`

### 6. **UI Layer** (`UserInterface/screens/`)
- Jetpack Compose screens
- Screens: `LoginScreen`, `PasienListScreen`, `PasienFormScreen`, `PasienDetailScreen`

---

## 🛠️ Technology Stack

### Core Technologies
- **Kotlin** - Bahasa pemrograman utama
- **Jetpack Compose** - UI framework modern Android
- **Material Design 3** - Design system

### Libraries & Dependencies

#### Database
- **Room Database 2.6.1** - Local database persistence
  - `room-runtime` - Core Room library
  - `room-ktx` - Kotlin coroutines support
  - `room-compiler` (kapt) - Code generation

#### Architecture Components
- **Lifecycle ViewModel 2.8.4** - ViewModel lifecycle management
- **Lifecycle LiveData 2.8.4** - Reactive data streams
- **Lifecycle ViewModel Compose 2.8.4** - Compose integration

#### Navigation
- **Navigation Compose 2.8.4** - Type-safe navigation

#### Image Handling
- **Coil 2.7.0** - Image loading library

#### UI Components
- **Material Icons Extended 1.7.6** - Extended icon set
- **Material Design 1.12.0** - Material components

### Build Tools
- **Gradle 8.13.2** - Build system
- **Kotlin 2.0.21** - Kotlin compiler
- **KAPT** - Kotlin Annotation Processing Tool

### Target SDK
- **Min SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 36
- **Compile SDK**: 36

---

## 🗄️ Struktur Database

### Tabel: `pasien`
Menyimpan data pasien utama.

| Field | Tipe | Deskripsi |
|-------|------|-----------|
| `id` | Long (Primary Key, AutoGenerate) | ID unik pasien |
| `namaLengkap` | String | Nama lengkap pasien |
| `nik` | String | Nomor Induk Kependudukan |
| `usia` | Int | Usia pasien |
| `jenisKelamin` | String | "Laki-laki" atau "Perempuan" |
| `alamat` | String | Alamat pasien |
| `nomorTelepon` | String | Nomor telepon |
| `catatanRiwayat` | String? | Catatan riwayat medis (nullable) |
| `fotoPath` | String? | Path ke foto pasien (nullable) |

### Tabel: `user`
Menyimpan data pengguna/admin aplikasi.

| Field | Tipe | Deskripsi |
|-------|------|-----------|
| `id` | Long (Primary Key, AutoGenerate) | ID unik user |
| `username` | String | Username untuk login |
| `password` | String | Password (plain text) |
| `role` | String | "Admin" atau "Petugas" |

**Default User:**
- Username: `admin`
- Password: `admin123`
- Role: `Admin`

### Tabel: `histori_visit`
Menyimpan riwayat kunjungan pasien.

| Field | Tipe | Deskripsi |
|-------|------|-----------|
| `id` | Long (Primary Key, AutoGenerate) | ID unik kunjungan |
| `pasienId` | Long (Foreign Key) | Referensi ke tabel pasien |
| `tanggalKunjungan` | Long | Timestamp tanggal kunjungan |
| `catatan` | String | Catatan kunjungan |
| `keluhan` | String? | Keluhan pasien (nullable) |

**Foreign Key:** `pasienId` → `pasien.id` (CASCADE on delete)

### Tabel: `obat`
Menyimpan data obat yang diresepkan untuk pasien.

| Field | Tipe | Deskripsi |
|-------|------|-----------|
| `id` | Long (Primary Key, AutoGenerate) | ID unik obat |
| `pasienId` | Long (Foreign Key) | Referensi ke tabel pasien |
| `namaObat` | String | Nama obat |
| `dosis` | String | Dosis obat |
| `frekuensi` | String | Frekuensi konsumsi |
| `catatan` | String? | Catatan tambahan (nullable) |

**Foreign Key:** `pasienId` → `pasien.id` (CASCADE on delete)

---

## 📱 Fitur-Fitur Aplikasi

### 1. **Autentikasi & Login**
- Login dengan username dan password
- Validasi kredensial dari database
- Batasan 3 kali percobaan login
- Default admin user otomatis dibuat saat pertama kali aplikasi dijalankan

### 2. **Manajemen Data Pasien**

#### **PasienListScreen** - Daftar Pasien
- Menampilkan daftar semua pasien
- **Fitur Pencarian:**
  - Pencarian berdasarkan nama lengkap
  - Pencarian berdasarkan NIK
- **Fitur Filter:**
  - Filter berdasarkan jenis kelamin
  - Filter berdasarkan rentang usia (min-max)
  - Kombinasi pencarian dan filter
- **Aksi:**
  - Tambah pasien baru
  - Lihat detail pasien
  - Edit pasien
  - Hapus pasien (dengan konfirmasi dialog)

#### **PasienFormScreen** - Form Tambah/Edit Pasien
- Form untuk menambah atau mengedit data pasien
- Input fields:
  - Nama lengkap
  - NIK
  - Usia
  - Jenis kelamin (radio button)
  - Alamat
  - Nomor telepon
  - Catatan riwayat
  - Foto pasien (dengan ImagePicker)

#### **PasienDetailScreen** - Detail Pasien
- Menampilkan informasi lengkap pasien
- Menampilkan foto pasien (jika ada)
- Menampilkan riwayat kunjungan
- Menampilkan daftar obat yang diresepkan
- Fitur untuk menambah/edit/hapus riwayat kunjungan
- Fitur untuk menambah/edit/hapus data obat

### 3. **Manajemen Riwayat Kunjungan**
- Tambah riwayat kunjungan baru
- Edit riwayat kunjungan
- Hapus riwayat kunjungan
- Menyimpan tanggal kunjungan, catatan, dan keluhan

### 4. **Manajemen Data Obat**
- Tambah data obat untuk pasien
- Edit data obat
- Hapus data obat
- Menyimpan nama obat, dosis, frekuensi, dan catatan

### 5. **Image Picker**
- Fitur untuk mengambil foto pasien
- Menggunakan camera atau gallery
- Permissions: CAMERA, READ_EXTERNAL_STORAGE, READ_MEDIA_IMAGES
- FileProvider untuk sharing file

---

## 📂 Struktur File Proyek

```
Project Akhir (MediCord)/
├── app/
│   ├── build.gradle.kts          # Konfigurasi build aplikasi
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/medicord/
│   │   │   │   ├── MainActivity.kt              # Entry point aplikasi
│   │   │   │   ├── Data/
│   │   │   │   │   ├── database/
│   │   │   │   │   │   ├── AppDatabase.kt       # Room database instance
│   │   │   │   │   │   ├── dao/                 # Data Access Objects
│   │   │   │   │   │   │   ├── PasienDao.kt
│   │   │   │   │   │   │   ├── UserDao.kt
│   │   │   │   │   │   │   ├── HistoriVisitDao.kt
│   │   │   │   │   │   │   └── ObatDao.kt
│   │   │   │   │   │   └── entity/                # Database entities
│   │   │   │   │   │       ├── PasienEntity.kt
│   │   │   │   │   │       ├── UserEntity.kt
│   │   │   │   │   │       ├── HistoriVisitEntity.kt
│   │   │   │   │   │       ├── ObatEntity.kt
│   │   │   │   │   │       └── converters/
│   │   │   │   │   │           └── DateConverter.kt
│   │   │   │   │   ├── model/                    # Data models
│   │   │   │   │   │   ├── Pasien.kt
│   │   │   │   │   │   ├── User.kt
│   │   │   │   │   │   ├── HistoriVisit.kt
│   │   │   │   │   │   └── Obat.kt
│   │   │   │   │   └── repository/               # Repository layer
│   │   │   │   │       ├── PasienRepository.kt
│   │   │   │   │       ├── UserRepository.kt
│   │   │   │   │       ├── HistoriVisitRepository.kt
│   │   │   │   │       └── ObatRepository.kt
│   │   │   │   ├── navigation/
│   │   │   │   │   └── NavGraph.kt               # Navigation configuration
│   │   │   │   ├── UserInterface/
│   │   │   │   │   └── screens/                  # UI Screens
│   │   │   │   │       ├── LoginScreen.kt
│   │   │   │   │       ├── PasienListScreen.kt
│   │   │   │   │       ├── PasienFormScreen.kt
│   │   │   │   │       └── PasienDetailScreen.kt
│   │   │   │   ├── ui/
│   │   │   │   │   └── theme/                    # Theme configuration
│   │   │   │   │       ├── Color.kt
│   │   │   │   │       ├── Theme.kt
│   │   │   │   │       └── Type.kt
│   │   │   │   ├── utils/
│   │   │   │   │   └── ImagePicker.kt            # Image picker utility
│   │   │   │   └── viewmodel/                     # ViewModels
│   │   │   │       ├── LoginViewModel.kt
│   │   │   │       ├── PasienViewModel.kt
│   │   │   │       ├── PasienFormViewModel.kt
│   │   │   │       └── PasienDetailViewModel.kt
│   │   │   └── res/                                # Resources
│   │   │       ├── drawable/
│   │   │       ├── values/
│   │   │       └── xml/
│   │   └── test/                                   # Unit tests
│   └── proguard-rules.pro
├── build.gradle.kts                                # Root build config
├── gradle/
│   ├── libs.versions.toml                         # Version catalog
│   └── wrapper/
├── settings.gradle.kts                             # Project settings
└── gradle.properties                               # Gradle properties
```

---

## 🔄 Alur Navigasi

```
LoginScreen
    ↓ (Login Success)
PasienListScreen
    ├─→ PasienFormScreen (Tambah/Edit)
    └─→ PasienDetailScreen
            ├─→ Form Tambah/Edit Histori Visit
            └─→ Form Tambah/Edit Obat
```

### Routes:
- `login` - Halaman login
- `pasien_list` - Daftar pasien
- `pasien_form/{pasienId}` - Form tambah/edit pasien (pasienId = 0 untuk tambah baru)
- `pasien_detail/{pasienId}` - Detail pasien

---

## 🔐 Permissions

Aplikasi memerlukan permissions berikut (didefinisikan di `AndroidManifest.xml`):

1. **CAMERA** - Untuk mengambil foto pasien
2. **READ_EXTERNAL_STORAGE** (maxSdkVersion 32) - Untuk membaca file dari storage
3. **WRITE_EXTERNAL_STORAGE** (maxSdkVersion 28) - Untuk menulis file ke storage
4. **READ_MEDIA_IMAGES** - Untuk membaca gambar dari media storage (Android 13+)

---

## 🎨 UI/UX Features

### Design System
- **Material Design 3** - Mengikuti design guidelines terbaru
- **Material Theme** - Custom theme dengan warna primary, secondary, dll
- **Edge-to-Edge** - Full screen experience

### Components
- **Cards** - Untuk container konten
- **OutlinedTextField** - Input fields dengan border
- **Buttons** - Material 3 buttons
- **Icons** - Material Icons Extended
- **Dialogs** - Confirmation dialogs untuk delete actions
- **LazyColumn** - Untuk list yang efisien
- **Image Loading** - Coil untuk loading gambar

---

## 🔧 Konfigurasi Database

### AppDatabase
- **Database Name**: `medicord_database`
- **Version**: 1
- **Migration Strategy**: `fallbackToDestructiveMigration()` (data akan dihapus saat schema berubah)
- **Type Converters**: `DateConverter` untuk konversi Date ke Long

### Singleton Pattern
Database menggunakan singleton pattern untuk memastikan hanya satu instance yang dibuat.

---

## 📝 State Management

### ViewModel dengan StateFlow
Setiap ViewModel menggunakan `StateFlow` untuk mengelola UI state:

```kotlin
data class PasienUiState(
    val pasienList: List<PasienEntity> = emptyList(),
    val filteredList: List<PasienEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedJenisKelamin: String? = null,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val pasienToDelete: PasienEntity? = null
)
```

### Reactive Data Flow
- Repository mengembalikan `Flow<List<T>>` untuk reactive updates
- ViewModel mengobservasi Flow dan update StateFlow
- UI mengobservasi StateFlow menggunakan `collectAsState()`

---

## 🚀 Cara Menjalankan Proyek

1. **Clone atau buka proyek** di Android Studio
2. **Sync Gradle** - Tunggu hingga semua dependencies terdownload
3. **Build Project** - Pastikan tidak ada error
4. **Run** - Jalankan di emulator atau device fisik
5. **Login** dengan kredensial default:
   - Username: `admin`
   - Password: `admin123`

---

## 📌 Catatan Penting

### Security
- ⚠️ **Password disimpan dalam plain text** - Untuk production, sebaiknya menggunakan hashing (bcrypt, SHA-256, dll)
- ⚠️ **Tidak ada enkripsi database** - Untuk data sensitif, pertimbangkan enkripsi Room database

### Performance
- Menggunakan `Flow` untuk reactive updates yang efisien
- `LazyColumn` untuk list yang panjang
- Image loading dengan Coil untuk caching dan optimization

### Best Practices
- ✅ MVVM architecture pattern
- ✅ Repository pattern untuk data abstraction
- ✅ Type-safe navigation
- ✅ StateFlow untuk state management
- ✅ Coroutines untuk async operations
- ✅ Room database untuk local persistence

---

## 🔮 Potensi Pengembangan

1. **Backend Integration** - Connect ke REST API untuk sync data
2. **Cloud Storage** - Upload foto ke cloud storage
3. **Export Data** - Export data pasien ke PDF/Excel
4. **Search Enhancement** - Full-text search dengan FTS
5. **Biometric Authentication** - Login dengan fingerprint/face recognition
6. **Offline Support** - Better offline handling dengan sync mechanism
7. **Data Encryption** - Encrypt sensitive data
8. **Backup & Restore** - Fitur backup dan restore data
9. **Multi-language** - Support bahasa Indonesia dan Inggris
10. **Dark Mode** - Tema gelap untuk aplikasi

---

## 📚 Referensi

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Material Design 3](https://m3.material.io/)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [ViewModel Guide](https://developer.android.com/topic/libraries/architecture/viewmodel)

---

**Dibuat untuk:** Semester 5 - Pengembangan Aplikasi Mobile  
**Project Akhir:** MediCord - Sistem Manajemen Data Pasien
