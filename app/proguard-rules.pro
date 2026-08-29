# EcoGuardianes - reglas ProGuard
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-dontwarn androidx.room.paging.**

# Enums usados en la capa de dominio y persistidos como texto
-keepclassmembers enum pe.ecoguardianes.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
