/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.modules.server;

import java.util.Set;

import org.apache.james.webadmin.Routes;
import org.apache.james.webadmin.dto.MappingSourceModule;
import org.apache.james.webadmin.mdc.RequestLogger;
import org.apache.james.webadmin.routes.AddressMappingRoutes;
import org.apache.james.webadmin.routes.AliasRoutes;
import org.apache.james.webadmin.routes.DomainMappingsRoutes;
import org.apache.james.webadmin.routes.DomainsRoutes;
import org.apache.james.webadmin.routes.ForwardRoutes;
import org.apache.james.webadmin.routes.GroupsRoutes;
import org.apache.james.webadmin.routes.MappingRoutes;
import org.apache.james.webadmin.routes.RegexMappingRoutes;
import org.apache.james.webadmin.routes.UserCreationRequestLogger;
import org.apache.james.webadmin.routes.UserRoutes;
import org.apache.james.webadmin.service.AggregateUserEntityValidator;
import org.apache.james.webadmin.service.DefaultUserEntityValidator;
import org.apache.james.webadmin.service.RecipientRewriteTableUserEntityValidator;
import org.apache.james.webadmin.service.UserEntityValidator;
import org.apache.james.webadmin.utils.JsonTransformerModule;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

public class DataRoutesModules extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Routes> routesMultibinder = Multibinder.newSetBinder(binder(), Routes.class);
        routesMultibinder.addBinding().to(AddressMappingRoutes.class);
        routesMultibinder.addBinding().to(AliasRoutes.class);
        routesMultibinder.addBinding().to(DomainsRoutes.class);
        routesMultibinder.addBinding().to(DomainMappingsRoutes.class);
        routesMultibinder.addBinding().to(ForwardRoutes.class);
        routesMultibinder.addBinding().to(GroupsRoutes.class);
        routesMultibinder.addBinding().to(MappingRoutes.class);
        routesMultibinder.addBinding().to(RegexMappingRoutes.class);
        routesMultibinder.addBinding().to(UserRoutes.class);

        Multibinder<JsonTransformerModule> jsonTransformerModuleMultibinder = Multibinder.newSetBinder(binder(), JsonTransformerModule.class);
        jsonTransformerModuleMultibinder.addBinding().to(MappingSourceModule.class);

        Multibinder.newSetBinder(binder(), RequestLogger.class).addBinding().to(UserCreationRequestLogger.class);

        Multibinder.newSetBinder(binder(), UserEntityValidator.class).addBinding().to(DefaultUserEntityValidator.class);
        Multibinder.newSetBinder(binder(), UserEntityValidator.class).addBinding().to(RecipientRewriteTableUserEntityValidator.class);
    }

    @Provides
    @Singleton
    UserEntityValidator userEntityValidator(Set<UserEntityValidator> validatorSet) {
        return new AggregateUserEntityValidator(validatorSet);
    }
}
