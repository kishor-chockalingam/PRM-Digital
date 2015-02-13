//
//  NSSet+HealthKitPermission.h
//  HealthkitTest
//
//  Created by CDC on 2/2/15.
//  Copyright (c) 2015 Accenture. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSSet(HealthKitPermission)
+ (NSSet*)setReadPermission;
+ (NSSet*)setWritePermission;
@end
