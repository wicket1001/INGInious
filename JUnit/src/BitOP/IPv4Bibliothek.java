package BitOP;

public class IPv4Bibliothek {
    public static void main(String[] args) {
        System.out.println("\nto32BitIp:");

        System.out.println(toString(to32BitIp("192.168.0.1")));
        System.out.println(toString(to32BitIp("10.10.0.1")));
        System.out.println(toString(to32BitIp("10.14.23.177"))); // Meine IP in der Schule

        System.out.println("\nto32BitIp:");

        System.out.println(toString(to32BitIp(new int[] {192, 168, 0, 1})));
        System.out.println(toString(to32BitIp(new int[] {10, 10, 0, 1})));
        System.out.println(toString(to32BitIp(new int[] {10, 14, 23, 177})));

        System.out.println("\ngetSuffix:");

        System.out.println("Die Suffix ist: " + getSuffix("192.168.0.0/8"));
        System.out.println("Die Suffix ist: " + getSuffix("192.168.0.0/16"));
        System.out.println("Die Suffix ist: " + getSuffix("192.168.0.0/26"));

        System.out.println("\ngetNetmask:");

        System.out.println(toString(getNetmask("192.168.0.0/8")));
        System.out.println(toString(getNetmask("192.168.0.0/16")));
        System.out.println(toString(getNetmask("192.168.0.0/26")));

        System.out.println("\ngetNetwork:");

        System.out.println(toHexString(getNetwork(0xc0a80a78, 8)));
        System.out.println(toHexString(getNetwork(0xc0a80a78, 16)));
        System.out.println(toHexString(getNetwork(0xc0a80a78, 24)));

        System.out.println("\ngetNetwork:");

        System.out.println(toString(getNetwork("192.168.10.120/8")));
        System.out.println(toString(getNetwork("192.168.10.120/16")));
        System.out.println(toString(getNetwork("192.168.10.120/24")));

        System.out.println("\ntoIntArray:");

        printIntArray(toIntArray(0xc0a80001), false);
        printIntArray(toIntArray(0xc0a80a78), false);
        printIntArray(toIntArray(0xffFFffFF), false);

        System.out.println("\ntoString:");

        System.out.println(toString(to32BitIp(new int[] {192, 168, 0, 1})));
        System.out.println(toString(to32BitIp(new int[] {192, 168, 10, 120})));
        System.out.println(toString(to32BitIp(new int[] {10, 14, 23, 177})));

        System.out.println("\ntoString:");

        System.out.println(toString(to32BitIp(new int[] {192, 168, 0, 1}), 8));
        System.out.println(toString(to32BitIp(new int[] {192, 168, 10, 120}), 16));
        System.out.println(toString(to32BitIp(new int[] {10, 14, 23, 177}), 26));

        System.out.println("\ntoHexString:");

        System.out.println(toHexString(0xc0a80001));
        System.out.println(toHexString(0xc0a80a78));
        System.out.println(toHexString(0xffFFffFF));

        System.out.println("\ntoHexString:");

        System.out.println(toHexString(0xc0a80001, 8));
        System.out.println(toHexString(0xc0a80a78, 15));
        System.out.println(toHexString(0xffFFffFF, 27));

        System.out.println("\ngetNextNetworks:");

        printIntArray(getNextNetworks(0x0a000000, 16, 4), true);
        System.out.println();
        printIntArray(getNextNetworks(0xc0a80a78, 27, 4), true);
        System.out.println();
        printIntArray(getNextNetworks(0x0a0e17b1, 27, 4), true);

        System.out.println("\ngetAllNetworksForNewSuffix:");

        printIntArray(getAllNetworksForNewSuffix(0x0a000000, 8, 10), true);
        System.out.println();
        printIntArray(getAllNetworksForNewSuffix(0xc0a80a78, 28, 30), true);
        System.out.println();
        printIntArray(getAllNetworksForNewSuffix(0x0a0e17b1, 27, 28), true);

        System.out.println("\ngetAllIpsInNetwork:");

        printIntArray(getAllIpsInNetwork(0xc0a80000, 30), true);
        System.out.println();
        printIntArray(getAllIpsInNetwork(0xc0a80a78, 30), true);
        System.out.println();
        printIntArray(getAllIpsInNetwork(0x0a0e17b1, 29), true);

        System.out.println("\nisInNetwork:");

        System.out.println(String.valueOf(isInNetwork("10.1.2.3", "10.0.0.0/8")));
        System.out.println(String.valueOf(isInNetwork("10.10.2.3", "10.10.0.0/16")));
        System.out.println(String.valueOf(isInNetwork("10.20.30.40", "10.20.0.0/24")));
    }

    /**
     * Gibt ein Int Array aus. Beachtet dabei ob im Int schon eine vollständige IP steht oder nur eine IP in Teilen.
     *
     * @param array Ein Int Array in dem die IP vollständig oder teilweise drin gespeichert wurde
     * @param fullIP Gibt an ob die IP vollständig ist
     */
    public static void printIntArray (int [] array, boolean fullIP) {
        if (fullIP) {
            for (int ip: array) {
                System.out.println(toString(ip));
            }
        } else {
            System.out.println(toString(to32BitIp(array)));
        }
    }

    /**
     * Die Methode wandelt eine IPv4-Adresse in eine 32 Bit-Zahl um.
     *
     * @param ip eine IP in der IPv4 Schreibweise ohne Suffix
     * @return Die ip in der 32 Bit Zahl
     */
    public static int to32BitIp(String ip) { // "192.168.0.1“ → 0xc0a80001 = 3.232.235.521;
        String octets [] = ip.split("[.]");
        if (octets.length != 4) throw new IllegalArgumentException("Wrong Parameter");
        int [] octet = new int [4];
        for (int i = 0; i < 4; i ++) {
            octet [i] = Integer.parseInt(octets [i]);
        }
        // int ipv4 = (int)Long.parseLong(ipDigit, 2);
        // System.out.println(0xffFFffFFL & (long)r);
            return to32BitIp(octet);
    }

    /**
     * Die Methode wandelt ein Array mit 4 Integer-Zahlen in eine 32 Bit-Zahl um.
     *
     * @param ip im 4 Integer-Zahlen Array ohne Suffix
     * @return Die ip in der 32 Bit Zahl
     */
    public static int to32BitIp(int[] ip) { // {192, 168, 0, 1} → 0xc0a80001 = 3.232.235.521
        int r = 0;
        for (int octet: ip) {
            if (octet < 0 || octet > 255) throw new IllegalArgumentException("Wrong Parameter");
            r = r * 256 + octet;
        }
        return r;
    }

    /**
     * Die Methode ermittelt das Suffix aus network.
     *
     * @param network Die Ip des Netzwerks in der a.b.c.d/suffix Schreibweise
     * @return Das Suffix vom Netzwerk
     */
    public static int getSuffix(String network) { // „192.168.0.0/16“ →16 (16 = Suffix)
        String s [] = network.split("[/]");
        if (s.length != 2 || Integer.parseInt(s[1]) < 0 || Integer.parseInt(s[1]) > 30) throw new IllegalArgumentException("Wrong Parameter");
        return Integer.parseInt(s[1]);
    }

    /**
     * Die Methode ermittelt zum network die Subnetmask als 32-Bit-Integer-Zahl.
     *
     * @param network Die Ip des Netzwerks in der a.b.c.d/suffix Schreibweise
     * @return Die Netzadresse des Netzwerks
     */
    public static int getNetmask(String network) { // „192.168.0.0/16“ → 0x ffff 0000 (wegen 16 Netzwerk-Bits (=Suffix))
        return -1 << 32 - getSuffix(network);
    }

    /**
     * Die Methode liefert die Netzwerk-Adresse für eine gegebene IP und das Suffix.
     *
     * @param ip in der 32 Bit Zahl ohne Suffix
     * @param suffix Das Suffix der Ip Adresse
     * @return Die Netzadresse des Netzwerks
     */
    public static int getNetwork(int ip, int suffix) { // „192.168.10.120“ bzw. 0xc0a80a78, 16 → 0xc0a80000
        return ip & -1 << 32 - suffix;
    }

    /**
     * Die Methode liefert die Netzwerk-Adresse aus network.
     *
     * @param network eine IP in der IPv4 Schreibweise mit Suffix
     * @return Die Netzadresse des Netzwerks
     */
    public static int getNetwork(String network) { // „192.168.10.120/16“ → 0xc0a80000
        return getNetwork(to32BitIp((network.split("[/]")) [0]), getSuffix(network));
    }

    /**
     * Die Methode wandelt eine 32-Bit-IP in ein Array mit den 4 IP-Teilen um.
     *
     * @param ip in der 32 Bit Zahl ohne Suffix
     * @return Die Ip Adresse als 4 Int Array
     */
    public static int[] toIntArray (int ip) { // 0xc0a80001 → {192, 168, 0, 1}
        return new int[] {
                255 & (ip >> 24),
                255 & (ip >> 16),
                255 & (ip >> 8),
                255 & (ip)
        };
    }

    /**
     * Die Methode wandelt die 32-Bit-IP in die normale (String-)Darstellung um.
     *
     * @param ip in der 32 Bit Zahl ohne Suffix
     * @return Die Ip Adresse in der IPv4 Schreibweise ohne Suffix
     */
    public static String toString (int ip) { // 0xc0a80001 → „192.168.0.1“
        return String.format("%d.%d.%d.%d",
                255 & (ip >> 24),
                255 & (ip >> 16),
                255 & (ip >> 8),
                255 & (ip)
        );
    }

    /**
     * Die Methode wandelt die 32-Bit-IP + gegeben Suffix in die normale (String-)Darstellung um.
     *
     * @param network in der 32 Bit Zahl ohne Suffix
     * @param suffix Das Suffix von dem Netzwerk
     * @return Die Ip Adresse in der IPv4 Schreibweise mit Suffix
     */
    public static String toString (int network, int suffix) { // 0xc0a80001,16→ „192.168.0.1/16“
        return toString(network) + "/" + suffix;
    }

    /**
     * Die Methode wandelt die 32-Bit-IP in eine Hexadezimale-String-Darstellung um.
     *
     * @param ip in der 32 Bit Zahl ohne Suffix
     * @return Die IP Adresse in einer Hexadezimalen Schreibweise ohne Suffix
     */
    public static String toHexString (int ip) { // „192.168.0.1“ bzw.0xc0a80001 → „c0.a8.00.01“
        return String.format("%02x.%02x.%02x.%02x",
               255 & (ip >> 24),
               255 & (ip >> 16),
               255 & (ip >> 8),
               255 & (ip)
       );
    }

    /**
     * Die Methode wandelt die 32-Bit-IP + gegeben Suffix in eine Hexadezimale-String-Darstellung um.
     *
     * @param network in der 32 Bit Zahl ohne Suffix
     * @param suffix Das Suffix der IP Adresse
     * @return Die IP Adresse in einer Hexadezimalen Schreibweise mit Suffix
     */
    public static String toHexString (int network, int suffix) { // 0xc0a80001,16→ „0a.80.00.01/10“ (16 Dezimal = 10 Hexadezimal)
        return toHexString(network) + "/" + suffix;
    }

    /**
     * Die Methode liefert zu der gegeben Network-Adresse und dem suffix die nächsten n Netzwerke in einem Array.
     *
     * @param network in der 32 Bit Zahl ohne Suffix
     * @param suffix Das Suffix der IP Adresse
     * @param n Die Anzahl der nächsten Netwerke
     * @return Die n nächsten Netzwerke mit der selben Suffix
     */
    public static int[] getNextNetworks (int network, int suffix, int n) { // 0x0a000000, 16, 4 → (dabei ist 0x0a000000 = „10.0.0.0“){0x0a 01 0000, 0x0a 02 0000, 0x0a 03 0000, 0x0a 04 0000}={„10. 1 .0.0“,  „10. 2 .0.0“, „10. 3 .0.0“, „10. 4 .0.0“}
        int [] networks = new int [n];
        for (int i = 0; i < n; ) {
            networks [i] = ((getNetwork(network, suffix) >>> 32 - suffix) + ++i) << 32 - suffix;
        }
        return networks;
    }

    /**
     * Die Methode liefert alle Netzwerke, die durch eine Vergrößerung des Suffix auf newSuffix möglich sind.
     *
     * @param network in der 32 Bit Zahl ohne Suffix
     * @param suffix Das Suffix der IP Adresse
     * @param newSuffix ein neues Suffix, welches kleiner sein muss, als das Original
     * @return Alle Subnetze mit dem neuen Suffix im Netzwerk mit der alten Suffix
     */
    public static int[] getAllNetworksForNewSuffix(int network, int suffix, int newSuffix) { // 0x0a000000, 8, 10 → (dabei ist 0x0a000000 = „10.0.0.0“){0x0a 0 00000,  0x0a 4 00000,  0x0a 8 00000, 0x0a c 00000}
        int n = (int) Math.pow(2, newSuffix - suffix); // ~getNetmask(network + "/" + suffix) / ~getNetmask(network + "/" + newSuffix);
        int [] networks = new int [n];
        networks [0] = getNetwork(network, suffix);
        System.arraycopy(getNextNetworks(network, newSuffix, n - 1), 0, networks, 1, n - 1);
        return networks;
    }

    /**
     * Die Methode liefert alle Adressen im Netzwerk mit dem angegebene Suffix (von der Netzerk-Adresse bis zur Broadcast-Adresse).
     *
     * @param network in der 32 Bit Zahl ohne Suffix
     * @param suffix Das Suffix der IP Adresse
     * @return Alle IPs im Netzwerk mit der Suffix
     */
    public static int[] getAllIpsInNetwork(int network, int suffix) { // 0xc0a80000, 30 →  (Dabei ist 0xc0a80000 = „192.168.0.0“.){ 0xc0a80000, 0xc0a80001, 0xc0a80002, 0xc0a80003
        int n = (int) Math.pow(2, 32 - suffix);
        int [] ips = new int [n];
        for (int i = 0; i < n; i++) {
            ips [i] = getNetwork(network, suffix) + i;
        }
        return ips;
    }

    /**
     * ist eine IP-Adresse in einem Netzwerk enthalen.
     *
     * @param ip Eine Ip die geprüft werden soll ob sie im Netzwerk in der a.b.c.d Schreibweise
     * @param network Eine Netzwerk in der a.b.c.d/suffix Schreibweise
     * @return ob die Ip im Netzwerk enthalten ist
     */
    public static boolean isInNetwork(String ip, String network) { // 10.1.2.3 ist im Netzwerk 10.0.0.0/8 enthalten, aber nicht im Netzwerk 10.0.0.0/16
        return getNetwork((to32BitIp(network.split("[/]") [0])), getSuffix(network)) == getNetwork(to32BitIp(ip), getSuffix(network));
    }
}
