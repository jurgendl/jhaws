package org.jhaws.common.g11n.currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jhaws.common.io.jaxb.JAXBMarshalling;

public class Currency {
    static JAXBMarshalling jaxb = new JAXBMarshalling(ISO_4217.class);

    public static void main(String[] args) {
        try {
            all().forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static SortedSet<CurrencyInfo> all() {
        ISO_4217 ISO_4217 = jaxb.unmarshall(Currency.class.getClassLoader().getResourceAsStream("g11n/currency/list_one.xml"));
        SortedSet<CurrencyInfo> arr = Arrays.asList(ISO_4217.getCcyTbl().getCcyNtry()).stream().filter(ci -> ci.getCode() != null).distinct().collect(Collectors.toCollection(TreeSet::new));
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Currency.class.getClassLoader().getResourceAsStream("g11n/currency/currency_symbol.csv")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                try {
                    String c = Arrays.stream(parts[1].split(",")).map(String::trim).map(hex -> Integer.parseInt(hex, 16)).map(i -> String.valueOf((char) (int) i)).collect(Collectors.joining());
                    arr.stream().filter(ci -> parts[0].equals(ci.getCode())).forEach(ci -> ci.setSign(c));
                } catch (RuntimeException ex) {
                    //
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return arr;
    }

    @XmlRootElement(name = "ISO_4217")
    static class ISO_4217 {
        CcyTbl CcyTbl;

        public CcyTbl getCcyTbl() {
            return this.CcyTbl;
        }

        public void setCcyTbl(CcyTbl ccyTbl) {
            this.CcyTbl = ccyTbl;
        }
    }

    @XmlRootElement(name = "CcyTbl")
    static class CcyTbl {
        CurrencyInfo[] CcyNtry;

        public CurrencyInfo[] getCcyNtry() {
            return this.CcyNtry;
        }

        public void setCcyNtry(CurrencyInfo[] ccyNtry) {
            this.CcyNtry = ccyNtry;
        }
    }

    @XmlRootElement(name = "CcyNtry")
    public static class CurrencyInfo implements Serializable, Comparable<CurrencyInfo> {
        private static final long serialVersionUID = -2880792331189684092L;

        @XmlElement(name = "CcyNm")
        private String name;

        @XmlElement(name = "Ccy")
        private String code;

        @XmlElement(name = "CcyNbr")
        private String number;

        private String sign;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNumber() {
            return this.number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getSign() {
            return this.sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        @Override
        public String toString() {
            return (this.code != null ? "code=" + this.code + ", " : "") + (this.name != null ? "name=" + this.name + ", " : "") + (this.number != null ? "number=" + this.number + ", " : "") + (this.sign != null ? "sign=" + this.sign : "");
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.code == null) ? 0 : this.code.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            CurrencyInfo other = (CurrencyInfo) obj;
            if (this.code == null) {
                if (other.code != null) return false;
            } else if (!this.code.equals(other.code)) return false;
            return true;
        }

        @Override
        public int compareTo(CurrencyInfo o) {
            return new CompareToBuilder().append(code, o.code).append(name, o.name).toComparison();
        }
    }
}
