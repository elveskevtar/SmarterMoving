// ==================================================================
// This file is part of Smart Moving.
//
// Smart Moving is free software: you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// Smart Moving is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Smart Moving. If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package net.smart.moving.config;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import net.smart.moving.SmartMoving;
import net.smart.properties.Properties;
import net.smart.properties.Property;

public class SmartConfig extends SmartProperties {
	
	private static final long serialVersionUID = 8024787486165092133L;
	private static final String _smartMovingPropertiesFileName = "smart_moving_options.txt";
	private static final String _sm_current = SmartMoving.VERSION;

	protected void loadFromProperties(Properties properties) {
		try {
			load(properties);
		} catch (Exception e) {
			throw new RuntimeException("Could not load Smart Moving properties from properties", e);
		}
	}

	protected void writeToProperties(Properties properties, List<Property<?>> except) {
		try {
			write(properties);

			if (except != null)
				for (int i = 0; i < except.size(); i++) {
					String key = except.get(i).getCurrentKey();
					if (key != null)
						properties.remove(key);
				}
		} catch (Exception e) {
			throw new RuntimeException("Could not write Smart Moving properties to properties", e);
		}
	}

	public void loadFromOptionsFile(File optionsPath) {
		Properties properties = new Properties(new File(optionsPath, _smartMovingPropertiesFileName));

		try {
			load(properties);
		} catch (Exception e) {
			throw new RuntimeException("Could not load Smart Moving properties from file", e);
		}
	}

	public void saveToOptionsFile(File optionsPath) {
		try {
			save(new File(optionsPath, _smartMovingPropertiesFileName), _sm_current, true, true);
		} catch (Exception e) {
			throw new RuntimeException("Could not save Smart Moving properties to file", e);
		}
	}

	@Override
	protected void printHeader(PrintWriter printer) {
		printer.println("#######################################################################");
		printer.println("#");
		printer.println("# Smart Moving mod configuration file");
		printer.println("# -----------------------------------");
		printer.println("#");
		printer.println("# Modify the values behind the keys in this file to configure the");
		printer.println("# Smart Moving behavior as you like it.");
		printer.println("#");
		printer.println("# * All options you leave at their default value will be automatically");
		printer.println("#   updated when the default value is updated with a new version of");
		printer.println("#   Smart Moving options.");
		printer.println("#");
		printer.println("# * All options you modify will stay the same over the upgrade cycles");
		printer.println("#   as long as they still fall in the allowed range.");
		printer.println("#");
		printer.println("# * The character '!' will be used after the value's text's end to");
		printer.println("#   mark a value that has originally been modified but became the");
		printer.println("#   default value at some point in the update process to avoid it being");
		printer.println("#   updated too with the next default value change.");
		printer.println("#");
		printer.println("# * The '!' mark can also be used to create a 'modified' and so not");
		printer.println("#   automatically updated value identical to the current default value");
		printer.println("#");
		printer.println("# Additionally you can create multiple option configurations and define");
		printer.println("# separate option values for each configuration.");
		printer.println("#");
		printer.println("# * key-value separator is ':'");
		printer.println("# * key-value-pair separator is ';'");
		printer.println("# * a value without key will become the default value");
		printer.println("#");
		printer.println("#######################################################################");
		printer.println();
	}

	@Override
	protected void printVersion(PrintWriter printer, String version, boolean comments) {
		if (comments)
			printer.println("# The current version of this Smart Moving options file");

		printer.print("move.options.version");
		printer.print(":");
		printer.print(version);
		printer.println();
	}
}
