-keep class com.google.android.gms.auth.api.identity.** { *; }

# Preserve Credential Manager classes
-keep class androidx.credentials.** { *; }
-keep class androidx.credentials.GetCredentialRequest { *; }
-keep class androidx.credentials.GetCredentialResponse { *; }
-keep class androidx.credentials.PrepareGetCredentialResponse { *; }
-keep class androidx.credentials.CredentialManager { *; }
-keep class com.google.android.gms.auth.api.signin.GoogleSignInAccount { *; }
-keep class com.google.android.gms.auth.api.signin.GoogleSignInOptions { *; }

# If you are using the prepareGetCredential() method, keep its result class
-keep class androidx.credentials.PrepareGetCredentialResponse { *; }
