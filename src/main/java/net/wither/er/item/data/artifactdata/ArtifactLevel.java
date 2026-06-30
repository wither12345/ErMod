package net.wither.er.item.data.artifactdata;

import net.minecraft.nbt.CompoundTag;

public record ArtifactLevel(int level, int experience, int total_experience) {
    public CompoundTag getTag(){
        CompoundTag ret = new CompoundTag();
        ret.putInt("level", this.level);
        ret.putInt("experience", this.experience);
        ret.putInt("total_experience", this.total_experience);
        return ret;
    }

    public static ArtifactLevel getByTag(CompoundTag tag){
        return new ArtifactLevel(tag.getInt("level"), tag.getInt("experience"), tag.getInt("total_experience"));
    }
}
