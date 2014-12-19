//
//  JawboneService.h
//  Hybris
//
//  Created by Accenture on 14-12-18.
//  Copyright (c) 2014年 Red Ant. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <UPPlatformSDK/UPPlatformSDK.h>

@interface JawboneService : UIViewController

+ (JawboneService *)sharedService;

- (void)startSessionCompletion:(UPPlatformSessionCompletion)sessionCompletion;

- (void)endCurrentSession;

- (void)validateSessionWithCompletion:(UPPlatformSessionCompletion)sessionCompletion;

@end
