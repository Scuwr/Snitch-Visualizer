package com.github.scuwr.snitchvisualizer;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

/**
 * The configuration settings for this SnitchVisualizer
 * 
 * @author Scuwr
 */
public class SVSettings {

	protected SV sv;

	public boolean listUpdate;
	public boolean updateDetection;
	public boolean renderEnabled;
	public float renderDistance;
	public float svSettingsKey;

	public SVSettings(SV sv) {
		listUpdate = false;
		updateDetection = true;
		renderEnabled = true;
		renderDistance = 6.0f;
		svSettingsKey = (float) Keyboard.KEY_V;
		this.sv = sv;
	}

	public static String getKeyDisplayString(int i) {
		if (i < 0) {
			return StatCollector.translateToLocalFormatted("key.mouseButton",
					new Object[] { Integer.valueOf(i + 101) });
		} else {
			return Keyboard.getKeyName(i);
		}
	}

	public void setOptionFloatValue(Options option, float f) {
		if (option == Options.RENDER_DISTANCE) {
			this.renderDistance = f;
		} else if (option == Options.SETTINGS_KEYBINDING) {
			this.svSettingsKey = f;
		}
	}

	public void setOptionValue(Options option) {
		if (option == Options.LIST_UPDATE) {
			this.listUpdate = !this.listUpdate;
		} else if (option == Options.UPDATE_DETECTION) {
			this.updateDetection = !this.updateDetection;
		} else if (option == Options.RENDER_ENABLED) {
			this.renderEnabled = !this.renderEnabled;
		}
	}

	public void setOptionValue(Options option, boolean b) {
		if (option == Options.LIST_UPDATE) {
			this.listUpdate = b;
		} else if (option == Options.UPDATE_DETECTION) {
			this.updateDetection = b;
		} else if (option == Options.RENDER_ENABLED) {
			this.renderEnabled = b;
		}
	}

	public boolean getOptionValue(Options option) {
		if (option == Options.LIST_UPDATE) {
			return this.listUpdate;
		} else if (option == Options.UPDATE_DETECTION) {
			return this.updateDetection;
		} else if (option == Options.RENDER_ENABLED) {
			return this.renderEnabled;
		}
		return false;
	}

	public float getOptionFloatValue(Options option) {
		if (option == Options.RENDER_DISTANCE) {
			return renderDistance;
		} else if (option == Options.SETTINGS_KEYBINDING) {
			return svSettingsKey;
		} else {
			return 0.0F;
		}
	}

	public String getKeyBinding(Options option) {
		String s = (new StringBuilder())
				.append(StatCollector.translateToLocal(option.getEnumString()))
				.append(": ").toString();

		if (option.getEnumFloat()) {
			float f = getOptionFloatValue(option);
			boolean b = getOptionValue(option);

			if (option == Options.RENDER_DISTANCE) {
				if (f == option.getValueMin()) {
					return (new StringBuilder()).append(s)
							.append(StatCollector.translateToLocal("MIN"))
							.toString();
				}

				if (f == option.getValueMax()) {
					return (new StringBuilder()).append(s)
							.append(StatCollector.translateToLocal("MAX"))
							.toString();
				} else {
					return (new StringBuilder()).append(s).append((int) (f))
							.append(" chunks").toString();
				}
			} else if (option == Options.RENDER_ENABLED) {
				if (b == true) {
					return (new StringBuilder())
							.append(s)
							.append(StatCollector
									.translateToLocal("options.on")).toString();
				} else {
					return (new StringBuilder())
							.append(s)
							.append(StatCollector
									.translateToLocal("options.off"))
							.toString();
				}
			} else if (option == Options.UPDATE_DETECTION) {
				if (b == true) {
					return (new StringBuilder())
							.append(s)
							.append(StatCollector
									.translateToLocal("options.on")).toString();
				} else {
					return (new StringBuilder())
							.append(s)
							.append(StatCollector
									.translateToLocal("options.off"))
							.toString();
				}
			} else if (option == Options.SETTINGS_KEYBINDING) {
				return (new StringBuilder()).append(s).append((int) (f))
						.toString();
			}

			if (f == 0.0F) {
				return (new StringBuilder()).append(s)
						.append(StatCollector.translateToLocal("options.off"))
						.toString();
			} else {
				return (new StringBuilder()).append(s).append((int) (f))
						.append("options.on").toString();
			}
		} else {
			return s;
		}
	}

	@SuppressWarnings("unused")
	private float parseFloat(String s) {
		if (s.equals("true")) {
			return 1.0F;
		}

		if (s.equals("false")) {
			return 0.0F;
		} else {
			return Float.parseFloat(s);
		}
	}

	public enum Options {
		LIST_UPDATE("svoptions.listUpdate", true, false), 
		UPDATE_DETECTION("svoptions.updateDetection", true, false), 
		RENDER_DISTANCE("svoptions.renderDistance", true, false, 1.0F, 6.0F, 1.0F), 
		RENDER_ENABLED("svoptions.renderEnabled", true, false),
		SETTINGS_KEYBINDING("svoptions.keyBinding", true, false);

		private final boolean enumFloat;
		private final boolean enumBoolean;
		private final String enumString;
		private final float valueStep;
		private float valueMin;
		private float valueMax;

		private Options(String s1, boolean flag, boolean flag1) {
			this.enumString = s1;
			this.enumFloat = flag;
			this.enumBoolean = flag1;
			this.valueMin = 0.0f;
			this.valueMax = 1.0f;
			this.valueStep = 0.0f;
		}

		private Options(String s1, boolean flag, boolean flag1, float valueMin,
				float valueMax, float valueStep) {
			this.enumString = s1;
			this.enumFloat = flag;
			this.enumBoolean = flag1;
			this.valueMin = valueMin;
			this.valueMax = valueMax;
			this.valueStep = valueStep;
		}

		public boolean getEnumFloat() {
			return this.enumFloat;
		}

		public boolean getEnumBoolean() {
			return this.enumBoolean;
		}

		public int returnEnumOrdinal() {
			return this.ordinal();
		}

		public String getEnumString() {
			return this.enumString;
		}

		public float getValueMax() {
			return this.valueMax;
		}

		public float getValueMin() {
			return this.valueMin;
		}

		public void setValueMax(float valueMax) {
			this.valueMax = valueMax;
		}

		public void setValueMin(float valueMin) {
			this.valueMin = valueMin;
		}

		public float normalizeValue(float p_148266_1_) {
			return MathHelper.clamp_float(
					(this.snapToStepClamp(p_148266_1_) - this.valueMin)
							/ (this.valueMax - this.valueMin), 0.0F, 1.0F);
		}

		public float denormalizeValue(float p_148262_1_) {
			return this.snapToStepClamp(this.valueMin
					+ (this.valueMax - this.valueMin)
					* MathHelper.clamp_float(p_148262_1_, 0.0F, 1.0F));
		}

		public float snapToStepClamp(float p_148268_1_) {
			p_148268_1_ = this.snapToStep(p_148268_1_);
			return MathHelper.clamp_float(p_148268_1_, this.valueMin,
					this.valueMax);
		}

		protected float snapToStep(float p_148264_1_) {
			if (this.valueStep > 0.0F) {
				p_148264_1_ = this.valueStep
						* (float) Math.round(p_148264_1_ / this.valueStep);
			}

			return p_148264_1_;
		}
	}
}
