package brightspark.spymod.item;

public enum EItemBasic
{
    BULLET("bullet"),
    GUN_BARREL("gunBarrel"),
    PISTOL_ASSEMBLY("pistolAssembly");

    public String name;
    public static final String[] allNames;

    static
    {
        allNames = new String[EItemBasic.values().length];
        for(EItemBasic e : EItemBasic.values())
            allNames[e.ordinal()] = e.name;
    }

    EItemBasic(String name)
    {
        this.name = name;
    }
}
