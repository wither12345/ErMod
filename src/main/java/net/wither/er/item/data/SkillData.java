package net.wither.er.item.data;

public record SkillData(int normal, int skill, int burst) {
    public static final SkillData DEFAULT = new SkillData(1, 1, 1);
}
