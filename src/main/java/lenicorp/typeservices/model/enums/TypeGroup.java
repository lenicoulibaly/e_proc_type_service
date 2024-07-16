package lenicorp.typeservices.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter @AllArgsConstructor
public enum TypeGroup
{
    TYPE_REGLEMENT("TYP_REG", "Type de règlement"),
    MOUVEMENT("TYP_MVT", "TYPE-MOUVEMENT"),
    TYPE_PRV("TYP_PRV", "TYPE-PRIVILEGE"),
    DOCUMENT("TYP_DOC", "TYPE-DOCUMENT"),
    MODE_REGLEMENT("TYP_MOD_REG", "MODE_REGLEMENT"),
    TYPE_CIVILITE("TYP_CIV", "Civilité"),
    TYPE_PIECE("TYP_PCE", "Type de pièce"),
    TYPE_FUNCTION("TYP_FNC", "Type de fonction"),
    TYPE_USER("TYP_USER", "Type d'utilisateur"), TYPE_STR("TYP_STR", "Type de structure"),
    TYPE_ECHEANCIER("TYPE_ECHEANCIER", "Type d'échéancier"),
    TYPE_FREQUENCE("TYPE_FREQUENCE", "Type de fréquences"),
    MODE_PRELEVEMENT("MODE_PRELEVEMENT", "mode de prélèvement");
    private String groupCode;
    private String groupName;

    public static boolean hasValue(String value)
    {
        if(value==null || value.trim().equals("")) return false;
        return Arrays.stream(TypeGroup.values()).map(tg->tg.name()).collect(Collectors.toList()).contains(value);
    }
}
