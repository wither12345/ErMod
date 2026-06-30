package net.wither.er.commands;

//@Mod.EventBusSubscriber
public class ShieldCommand {
	/*
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		ErMod.LOGGER.info(AdditionalRegistries.SHIELD.registry());
		ErMod.LOGGER.info(AdditionalRegistries.SHIELD.location());


		event.getDispatcher().register(Commands.literal("shield").requires(s -> s.hasPermission(2))
				//entity
				.then(Commands.argument("entity", EntityArgument.entities())
						//command clear
						.then(Commands.literal("clear").executes(arguments -> {
							//nEntity entity = EntityArgument.getOptionalEntities(arguments, "entity");
							Collection<? extends Entity> entities = EntityArgument.getEntities(arguments, "entity");
							for (Entity entity : entities) {
								if (entity instanceof ErEntityInterface living) {
									living.cleanShield();
									//List<ErShield> shields = ErShieldEntity.getShields(living);
									//for (ErShield shield : shields) {
									//	ErShieldEntity.removeShield(living, shield);
									//}
								}
							}
							return 0;
						}))
						//command add
						.then(Commands.literal("add")
								//shield multi
								.then(Commands.argument("shield", ResourceArgument.resource(event.getBuildContext(), AdditionalRegistries.SHIELD))
										//shield health
										.then(Commands.argument("health", FloatArgumentType.floatArg())
												//shield time
												.then(Commands.argument("time", IntegerArgumentType.integer()).executes(arguments -> {
													Collection<? extends Entity> entities = EntityArgument.getEntities(arguments, "entity");
													ErShield shield = ResourceArgument.getResource(arguments, "shield", AdditionalRegistries.SHIELD).value();
													float health = FloatArgumentType.getFloat(arguments, "health");
													int time = IntegerArgumentType.getInteger(arguments, "time");
													for (Entity entity : entities) {
														if (entity instanceof ErEntityInterface living)
															living.addShield(new ShieldStack(shield, health, time));
														//ErShieldEntity.addShield(living, new ShieldStack(shield, health, time));
													}
													return 0;
												})))))
						//command remove
						.then(Commands.literal("remove")
								//shield multi
								.then(Commands.argument("shield", ResourceArgument.resource(event.getBuildContext(), AdditionalRegistries.SHIELD)).executes(arguments -> {
									Collection<? extends Entity> entities = EntityArgument.getEntities(arguments, "entity");
									ErShield shield = ResourceArgument.getResource(arguments, "shield", AdditionalRegistries.SHIELD).value();
									for (Entity entity : entities) {
										if (entity instanceof ErEntityInterface living)
											living.removeShield(shield);
										//if (entity instanceof LivingEntity living)
										//	ErShieldEntity.removeShield(living, shield);
									}
									return 0;
								})))));
	}

	 */
}