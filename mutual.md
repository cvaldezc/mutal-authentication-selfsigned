Mutual Authentication
======================

# ¿Qué es TLS mutuo?

El TLS mutuo, abreviado como mTLS, es una método de autenticación mutua. el MTLS garantiza que las partes de cada extremo de una conexión de red son quienes dicen ser, verificando que ambas tienen la clave privada correcta. La información incluida en sus respectivos certificados TLS proporciona una verificación adicional. mTLS se suele utilizar en un marco de seguridad Zero Trust * para verificar los usuarios. dispositivos y servidores dentro de una organización. Tambien, puede ayudar a antener la seguridad de las API.

- * Zero Trust, significa que no se confía por defecto en ningún usuario, dispositivo o tráfico de red, un enfoque que ayuda a eliminar muchas vulnerabilidades de seguridad.

# ¿Qué es TLS?

Transport Layer Security (TLS), es un protocolo de encriptación que se usa mucho en internet. TLS, anteriormente conocido como SSL, autentica al servidor en una conexión cliente-servidor, y encripta las comunicaciones entre el cliente y el servidor para que partes externas no puedan espiar las comunicaciones.

Hay tres cosas importantes que hay que entender cómo funciona TLS:

## 1. Clave pública y clave privada

TLS funciona con una técnica conocida como **criptografía de clave pública**, que se basa en un par de claves: una clave pública y una clave privada. Todo lo que se encripte con la clave pública solo se podrá desencriptar con la clave privada.
Por lo tanto, un servidor que desencripta un mensaje encriptado con la clave pública demuestra que posee la clave privada. Cualquiera puede ver la clave pública consultando el certificado TLS del dominio o del servidor.

## 2. Certificado TLS

Un certificado TLS es un archivo de datos que contine información importante para verificar la identidad de un servidor o dispositivo, incluyendo la clave pública, una declaración de quién emitió el certificado (los certificados TLS los emite una agencia certificadora) y la fecha de caducidad del certificado.

## 3. Protocolo de enlace TLS

El protocolo de enlace TLS es el proceso para verificar el certificado TLS y la posesión de la clave privada por parte del servidor. El protocolo de enlace TLS también establece cómo se llevará a cabo la encriptacion una vez haya finalizado el protocolo de enlace.

# ¿Cómo funciona mTLS?

Lo normal es que en TLS el servidor tenga un certificado TLS y un par de claves públicas/privadas, mientras que el cliente no los tiene. El proceso típico de TLS dunciona de la siguiente manera:

1. El cliente se conecta al servidor
2. El servidor presenta su certificado TLS
3. El cliente verifica el certificado del servidor
4. El cliente y el servidor intercambian información a través de ina conexión TLS encriptada.

![TLS normal](./imgs/how_tls_works-what_is_mutual_tls.png)

Sin embargo, en mTLS, tanto el cliente como el servidor tienen un certificado, y ambas partes se autentican utilizando su par de claves públicas/privadas. En comparación con el TLS normal, en mTLS hay pasos adicionales para verificar a ambas partes.

1. El cliente se conecta al servidor.
2. El servidor presenta su cetificado TLS
3. El cliente verifica el certificado del servidor
4. El cliente presetna su certificado TLS
5. El servidor verifica el certificado del cliente
6. El servidor concede el acceso
7. El cliente y el servidor intercambian información a través de una conexión TLS encriptada.

![mTLS](./imgs/how_mtls_works-what_is_mutual_tls.png)

Por que utilizar mTLS, ayuda a garantizar el tráfico sea seguro y de confianza en ambas direcciones entre un cliente y un servidor. Esto ofrece una capa adicional de seguridad para los usuarios que se conectan a la red o a las aplicaciones de una organización. También, verifica las conexiones con dispositivos cliente que no siguen un proceo de inicio de sesion, como los dispositivos del internet de las cosas.

- **Ataque en ruta**: los atacantes en ruta se sitúan entre un cliente y un servidor, e interceptan o modifican las comunicaiones entre ambos. Cuando se utiliza mTLS, los atancastes en ruta no pueden autenticar no al cliente no al servidor, lo que hace que este ataque sea casi imposible de llevar acabo.

- **Ataques de suplantanción**: los atacantes pueden intentar suplantar (imitar) un servidor web a un usuario, o viceversa. Los ataques de suplantación son mucho más complicdos cuando ambas partes tienen que autenticarse con certificados TLS.

- **Relleno de credenciales**: los atacantes utilizan conjuntos de credenciales filtrados de una fuga de datos para intentar iniciar sesión como un usuario legítimo. Sin un certificado TLS emitido de fora legítimo, los ataques de relleno de credenciales no pueden tener éxito contra las organizaciones que utilizan mTLS.

- **Ataques de fuerza bruta**: Llevado a cabo normalmente con bots, un ataque de fuerza bruta es cuando un atancante utiliza el método de ensayo y error rápido para adivinar la contraseña de un usuario. mTLS garantiza que una contraseña no se suficiente para acceder a la red de una organización.

- **Ataques de phishing**, el objetivo de un ataque de phishing suele ser robar las credenciales del usuario, y luego utilizarlas para poner en riesgos una red o una aplicación. Incluso si un usuario si un usuario cae en una ataque de este tipo, el atancante sigue necesitando un certificado TLS y una clave privada correspondiente para poder utilizar esas credenciales.

- **Peticiones maliciosas de la API**, cuando se utiliza para la seguridad de API, mTLS garantiza que las solicitudes de la API procedan únicamente de usuarios legítimos y autenticados. Esto impide que los atacantes envién solicitudes de API maliciosas que tengan como objetivo aprovechar la vulnerabilidad o subvertir la forma en que se supone que funciona la API.

 Casos de solución

### 1. Criptografía pública con llaves asimétricas

Está basada en el uso del algoritmo de llave pública RSA donde se tienen dos llaves:

- Llave pública para cifrar el mensaje: Esta llave la puede tener cualquiera que se desee comunicar con "PC B".
- Llave privada para descifrar el mensaje: Esta llave solo la debe tener "PC B". Con esto se garantiza que solo "PC B" puede descifrar el mensaje.

El objetivo es que el mensaje sea cifrado desde el computador de "PC A" con la llave pública y llegue a el computador "PC B" para ser descifrado con la llave privada. Para esto, se requiere que PC B previamente le haya compartido su llave pública a PC A. Este esquema asegura que el mensaje no puede ser leído por algún dispositovo de la red o programas espías.

![Asymetric public key](./imgs/1_criptografía_public_key_asymetric.png)

Este proceso tiene una debilidad y es que no le permite a PC B enviarle mensajes de respuesta cifrados a PC A. Para esto PC B tendría que tener una llava que solo PC A entienda. Adicionalmente, PC A no sabe si la llave que recibió de PC B es realmente de PC B.


### 2. Criptografía de llave simétrica

Este mecanismo resuelve el problema anterior donde PC B no podía a PC A mensajes de respuesta cifrados. Esta solución permite usar la misma llave para cifrar y descifrar el mensaje en ambos sentidos.
Es este escenario PC Ay PC B poseen una copia de llave lo cual les permite intercambiar mensajes cifrados entre los dos, pero sigue existiendo un problema, ¿cómo sabe PC A que es PC B realmente?

![Symentric public key](./imgs/2_criptografía_public_key_symetric.png)

Puede pasar que un computador de la red se haga pasar con PC B y PC A intercambie mensajes con el impostor. Este tipo de vulnerabilidad se conoce como *hombre en el medio*. Otro problema es que si alguien captura la llave puede leer los mensajes que envíe PC B o PC A.

### 3. TLS con certificados digitales

TLS como protocolo de seguridad que estable un canal seguro de envío de información cifrada entre dos componentes conectados a través de internet o un a red interna usando el protocolo http u otros protocolos de intercambio de mensaje.
Esta es la evolución de SSL (Secure Socket Layer) y básicamente se diferencia de SSL por sus nuevas suites de cifrado y mejoras de seguridad en la funcionalidad del protocolo.
Esta solución  se basa en uso de cerificados digitales con formato X.509, que son llaves asimétricas, pero con información adicional donde incluye ua firma digital de un tercero siguiendo es estándar RFC5280.
TLS cuando se cifra con las llaves RDS lo que se intercambia son otras llaves que mejoran la seguridad y el performance.
Adicionalmente, para asegurar la confiabilidad de los cetificados digirales deben ser firmados por una entidad certificadora. también, conocida como CA(Certificate Authority). Su principal función es garantizar que la llaves públicas sean de quien dice ser. Las entidades certificadoras pueden ser de dos tipos.
	- **Internas o locales**, proporcionan certificados figitales aceptados internamente por los sistemas de una organización. La confianza en la entidad se pueden dar cargando manualmente los certificados de la entidad en los sistemas o cargándolos a través de políticas de directorio activo.
	- **Externas**, proporciona certificados digitales que sean válidos por fuera del dominio de la entidad. Es usado para comunicarse con otras aplicaciones por fuera del dominio.

PC B comparte su certificado digital con PC A donde valida con la CA que el certificado digital de llave pública sea de A. Una vez validado, A cifra y envía el mensake a B para que se descifrado con su certificado diital de llave privada.

![TLS con certificado digitales](./imgs/TLS_with_certificate_digital.png)

Con esta solución se resulve el problema que PC-A realmente reconozca a PC-B mediante la validación del certificado a través de una CA.
Ahora bien, como sabe PC-B que PC-A es realmente PC-A. es aquí donde juega un papel importande la autenticación mutua.

#### 4. TLS con Autenticación mutua

Autenticación mutua o Mutual TLS (mTLS), o también conocida como autenticación en dos caminos, se refiere a la autenticación en ambas puntas de la comunicación al mismo tiempo.
El protocolo TLS soporta de forma nativa autenticar al servidor o la autenticación mutua, pero por defecto está solo autenticación al servidor. Con mTLS también se puede validar la identidad del cliente ante el servidor. Para el caso del ejemplo, PC-B sabría que el mensaje es realmente enviado por PC-A

![Mutal TLS](./imgs/mTLS-with-digital-certificates.png)

Cabe aclarar que mTLS no es el único mecanismo para validar la identidad del cliente. Hay otros mecanismos como Basic Autheticatin, Bearer Token, API Key y Secret entre otros. Pero el objetivo de este articulo es dar a conocer un métdo de autenticación robusto con autenticación mutua (mTLS).


- **KeyStore**: Almacena la llave privada de nuestro certificado.
- **Truestore**: Almacena llaves públicas y certificados que el *trustore* se encuentre en un archivo independiente del keystore.
